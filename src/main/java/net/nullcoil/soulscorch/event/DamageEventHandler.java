package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Blocks;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class DamageEventHandler {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, amount, originalAmount, blocked) -> {
            // Check if damage came from soul fire
            if (isTouchingSoulFire(entity)) {
                // Apply Soulscorch effect only if not already active
                if (!entity.hasStatusEffect(ModEffects.SOULSCORCH)) {
                    entity.addStatusEffect(new StatusEffectInstance(
                            ModEffects.SOULSCORCH,
                            200, // Duration in ticks (10 seconds)
                            0,   // Amplifier
                            false, // Show particles
                            true,  // Show icon
                            true   // Can be removed by milk
                    ));
                }
            }
        });
    }

    private static boolean isTouchingSoulFire(LivingEntity entity) {
        // Check if entity is standing in soul fire or on a soul campfire
        return entity.getWorld().getBlockState(entity.getBlockPos()).getBlock() == Blocks.SOUL_FIRE ||
                entity.getWorld().getBlockState(entity.getBlockPos().down()).getBlock() == Blocks.SOUL_FIRE ||
                entity.getWorld().getBlockState(entity.getBlockPos().down()).getBlock() == Blocks.SOUL_CAMPFIRE ||
                entity.getWorld().getBlockState(entity.getBlockPos()).getBlock() == Blocks.SOUL_CAMPFIRE;
    }
}