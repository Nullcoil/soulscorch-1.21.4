package net.nullcoil.soulscorch.event.handlers;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.custom.RestlessEntity;
import net.nullcoil.soulscorch.item.ModItems;

import java.util.List;

public class DamageEventHandler {
    public static RestlessEntity chooseRestless(LivingEntity entity) {
        if(entity.getWorld() instanceof ServerWorld world && entity instanceof PlayerEntity) {
            Box searchBox = new Box(
                    entity.getX() - 24, entity.getY() - 24, entity.getZ() - 24,
                    entity.getX() + 24, entity.getY() + 24, entity.getZ() + 24
            );

            List<RestlessEntity> awakenedRestless = world.getEntitiesByClass(RestlessEntity.class, searchBox, r-> r.getAwakened());
            List<RestlessEntity> candidates = world.getEntitiesByClass(RestlessEntity.class, searchBox, r -> !r.getAwakened());

            if(awakenedRestless.isEmpty() && !candidates.isEmpty()) {
                return candidates.get(world.random.nextInt(candidates.size()));
            }
        }
        return null;
    }

    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((LivingEntity entity,
                                                        net.minecraft.entity.damage.DamageSource source,
                                                        float baseDamage,
                                                        float damageTaken,
                                                        boolean blocked) -> {
            RestlessEntity toAwaken = chooseRestless(entity);
            if(toAwaken != null) {
                toAwaken.setAwakened(true);
            }
            // Only proc if entity has Soulscorch AND actually took damage
            if (
                    !entity.hasStatusEffect(ModEffects.SOULSCORCH) ||
                    entity.hasStatusEffect(ModEffects.CAT_BUFF) ||
                    damageTaken <= 0.0f) {
                return; // ignore the Soulscorch penalty
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
}
