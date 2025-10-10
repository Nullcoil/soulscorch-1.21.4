package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.custom.RestlessEntity;
import net.nullcoil.soulscorch.item.ModItems;
import net.nullcoil.soulscorch.util.BlockUtils;

import java.util.List;

public class DamageEventHandler {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((LivingEntity entity,
                                                        net.minecraft.entity.damage.DamageSource source,
                                                        float baseDamage,
                                                        float damageTaken,
                                                        boolean blocked) -> {
            // Awaken nearby Restless entities
            if (entity.getWorld() instanceof ServerWorld serverWorld && entity instanceof PlayerEntity) {
                Box searchBox = new Box(
                        entity.getX() - 24, entity.getY() - 24, entity.getZ() - 24,
                        entity.getX() + 24, entity.getY() + 24, entity.getZ() + 24
                );

                List<RestlessEntity> candidates = serverWorld.getEntitiesByClass(RestlessEntity.class, searchBox, r -> !r.getAwakened());

                List<RestlessEntity> allRestless = serverWorld.getEntitiesByClass(
                        RestlessEntity.class, searchBox, r -> true
                );
                System.out.println("[DEBUG] All nearby Restless: " + allRestless.size());
                for (RestlessEntity r : allRestless) {
                    System.out.println("[DEBUG] Restless at " + r.getBlockPos() + " awakened=" + r.getAwakened());
                }

                if (!candidates.isEmpty()) {
                    RestlessEntity toAwaken = candidates.get(serverWorld.random.nextInt(candidates.size()));
                    toAwaken.setAwakened(true);
                }
            }
            // Only proc if entity has Soulscorch AND actually took damage
            if (!entity.hasStatusEffect(ModEffects.SOULSCORCH) || damageTaken <= 0.0f) {
                return;
            }

            if ( entity instanceof PlayerEntity player) {
                boolean totemFound = false;

                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getStackInHand(hand);
                    if(stack.getItem() == ModItems.SOULWARD_TOTEM) {
                        EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                        stack.damage(5,player, slot);
                        totemFound = true;
                        break;
                    }
                }

                if (!totemFound) {
                    EntityAttributeInstance maxHealthAttr = entity.getAttributeInstance(EntityAttributes.MAX_HEALTH);
                    if (maxHealthAttr != null) {
                        double currentMax = maxHealthAttr.getBaseValue();
                        double newMax = Math.max(1.0, currentMax - 1.0); // Reduce by 1 health point

                        if (newMax < currentMax) {
                            maxHealthAttr.setBaseValue(newMax);
                            if (entity.getHealth() > (float) newMax) {
                                entity.setHealth((float) newMax);
                            }
                        }
                    }
                }
            }
        });
    }

    private static boolean isTouchingSoulCampfire(LivingEntity entity) {
        BlockState standingOn = BlockUtils.getBlockUnderEntity(entity);
        return standingOn != null && standingOn.isOf(Blocks.SOUL_CAMPFIRE);
    }
}
