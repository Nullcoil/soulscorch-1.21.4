package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.util.BlockUtils;
import java.util.List;

public class SoulCampfireDetector {

    /**
     * Call once during mod initialization.
     * Iterates all loaded LivingEntity instances in the world and applies the Soulscorch
     * effect when an entity is standing on a lit soul campfire.
     */
    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register((ServerWorld world) -> {
            try {
                // Collect all loaded living entities in the world
                List<LivingEntity> livingEntities =
                        (List<LivingEntity>) world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), entity -> true);

                for (LivingEntity livingEntity : livingEntities) {
                    BlockState under = BlockUtils.getBlockUnderEntity(livingEntity);
                    if (under == null) continue;

                    // If it's a lit soul campfire, apply the Soulscorch effect
                    if (under.isOf(Blocks.SOUL_CAMPFIRE) && CampfireBlock.isLitCampfire(under)) {
                        // Apply the exact StatusEffectInstance you requested
                        livingEntity.addStatusEffect(new StatusEffectInstance(
                                ModEffects.SOULSCORCH,
                                600, // Duration in ticks (30 seconds)
                                0,   // Amplifier
                                false, // is Ambient
                                false,  // show Particles
                                true   // show Icon
                        ));
                    }
                }
            } catch (Throwable t) {
                Soulscorch.LOGGER.error("[SoulCampfireDetector] error during tick check", t);
            }
        });
    }
}
