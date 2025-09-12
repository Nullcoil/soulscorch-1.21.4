package net.nullcoil.soulscorch;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.effect.SoulscorchEffect;
import net.nullcoil.soulscorch.event.DamageEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Soulscorch implements ModInitializer {
	public static final String MOD_ID = "soulscorch";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModEffects.registerEffects();
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (entity.hasStatusEffect(ModEffects.SOULSCORCH)) {
                double newMax = entity.getAttributeValue(EntityAttributes.MAX_HEALTH) - 1.0;
                if (newMax < 1.0) newMax = 1;

                entity.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(newMax);

                if (entity.getHealth() > newMax) {
                    entity.setHealth((float) newMax);
                }
            }
        });
	}
}