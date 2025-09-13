package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.util.BlockUtils;

public class DamageEventHandler {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((LivingEntity entity,
                                                        DamageSource source,
                                                        float baseDamage,
                                                        float damageTaken,
                                                        boolean blocked) -> {
            // Only proc if entity has Soulscorch AND actually took damage
            if (!entity.hasStatusEffect(ModEffects.SOULSCORCH) || damageTaken <= 0.0f) {
                return;
            }

            // Check block under entity using BlockUtils
            if (isTouchingSoulCampfire(entity)) {
                entity.addStatusEffect(new StatusEffectInstance(
                        ModEffects.SOULSCORCH,
                        600, // Duration in ticks (30 seconds)
                        0,   // Amplifier
                        true, // Show particles
                        true, // Show icon
                        false // Can be removed by milk
                ));
            }

            EntityAttributeInstance maxHealthAttr = entity.getAttributeInstance(EntityAttributes.MAX_HEALTH);
            if (maxHealthAttr == null) return;

            double currentMax = maxHealthAttr.getBaseValue();
            // Reduce max health by half a heart (1.0 health point), min 0.5 hearts (1.0 health)
            double newMax = Math.max(1.0, currentMax - 1.0);

            if (newMax < currentMax) {
                maxHealthAttr.setBaseValue(newMax);

                // Clamp current health to the new max if necessary
                if (entity.getHealth() > (float)newMax) {
                    entity.setHealth((float)newMax);
                }
            }
        });
    }

    private static boolean isTouchingSoulCampfire(LivingEntity entity) {
        BlockState standingOn = BlockUtils.getBlockUnderEntity(entity);
        return standingOn != null && standingOn.isOf(Blocks.SOUL_CAMPFIRE);
    }
}
