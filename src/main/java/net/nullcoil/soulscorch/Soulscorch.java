package net.nullcoil.soulscorch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.EntityTypeTags;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.block.entity.ModBlockEntities;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;
import net.nullcoil.soulscorch.entity.custom.RestlessEntity;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;
import net.nullcoil.soulscorch.event.*;
import net.nullcoil.soulscorch.item.ModItems;
import net.nullcoil.soulscorch.potion.ModPotions;
import net.nullcoil.soulscorch.screen.ModScreenHandlers;
import net.nullcoil.soulscorch.sound.ModSounds;
import net.nullcoil.soulscorch.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.crystalnest.soul_fire_d.api.FireManager;

public class Soulscorch implements ModInitializer {
    public static final String MOD_ID = "soulscorch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModEffects.register();
        ModEntities.register();
        ModEvents.register();

        ModItems.register();
        ModPotions.register();
        ModScreenHandlers.register();
        ModSounds.register();

        ModWorldGeneration.register();



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
