package net.nullcoil.soulscorch.block;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.EntityTypeTags;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.effect.ModEffects;

public class SoulFireReregisterer {

    public static void register() {
        Soulscorch.LOGGER.info("Re-registering Soul Fire for " + Soulscorch.MOD_ID);
        // Re-registering Soul Fire'd's Soul Fire to be used in this mod
        FireManager.unregisterFire(FireManager.SOUL_FIRE_TYPE);
        FireManager.registerFire(
                FireManager.fireBuilder(FireManager.SOUL_FIRE_TYPE)
                        .setLight(10)
                        .setDamage(2)
                        .setBehavior(entity -> {
                                    if (entity.isOnFire()
                                            && !entity.getType().isIn(EntityTypeTags.UNDEAD)
                                            && entity instanceof LivingEntity livingEntity) {
                                        livingEntity.addStatusEffect(new StatusEffectInstance(
                                                ModEffects.SOULSCORCH,
                                                600, // Duration in ticks (30 seconds)
                                                0,   // Amplifier
                                                false, // is Ambient
                                                false,  // Show Particles
                                                true   // Show Icon
                                        ));
                                    }
                                }
                        )
                        .build()
        );
    }
}
