package net.nullcoil.soulscorch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;
import net.nullcoil.soulscorch.event.DamageEventHandler;
import net.nullcoil.soulscorch.event.SleepHealthResetHandler;
import net.nullcoil.soulscorch.item.ModItems;
import net.nullcoil.soulscorch.potion.ModPotions;
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
        ModItems.register();
        ModEffects.registerEffects();
        DamageEventHandler.register();
        SleepHealthResetHandler.register();
        SoulCampfireDetector.register();
        ModPotions.register();
        ModEntities.register();
        ModSounds.register();
        ModWorldGeneration.generateModWorldGen();

        FabricDefaultAttributeRegistry.register(ModEntities.BLAZT, BlaztEntity.createBlaztAttributes());

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
                                                true, // Show particles
                                                true,  // Show icon
                                                false   // Can be removed by milk
                                        ));

                                    }
                                }
                        )
                        .build()
        );

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModItems.SOUL_CREAM, ModPotions.SOUL_RENDER_POTION);
        });
    }
}
