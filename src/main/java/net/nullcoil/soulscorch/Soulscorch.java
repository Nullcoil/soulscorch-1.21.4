package net.nullcoil.soulscorch;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.tag.EntityTypeTags;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.nullcoil.soulscorch.event.DamageEventHandler;
import net.nullcoil.soulscorch.event.SleepHealthResetHandler;
import net.nullcoil.soulscorch.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.crystalnest.soul_fire_d.api.FireManager;

public class Soulscorch implements ModInitializer {
    public static final String MOD_ID = "soulscorch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEffects.registerEffects();
        DamageEventHandler.register();
        SleepHealthResetHandler.register();
        SoulCampfireDetector.register();

        // In your mod loader class
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
                                                true, // Show particles
                                                true,  // Show icon
                                                false   // Can be removed by milk
                                        ));

                                    }
                                }
                        )
                        .build()
        );
    }
}
