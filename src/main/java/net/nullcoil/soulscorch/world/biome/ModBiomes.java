package net.nullcoil.soulscorch.world.biome;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.*;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.world.gen.feature.ModPlacedFeatures;

public class ModBiomes {
    public static final RegistryKey<Biome> SOULVORE_CAVERNS = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(Soulscorch.MOD_ID, "soulvore_caverns"));

    public static void bootstrap(Registerable<Biome> context) {
        context.register(SOULVORE_CAVERNS, soulvoreCaverns(context));
    }

    public static Biome soulvoreCaverns(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        // spawnBuilder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(ModEntities.HYTODOM, 1, 1, 5));

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        // FIXED: Use the new carver API for 1.21.4
        biomeBuilder.carver(ConfiguredCarvers.NETHER_CAVE);

        // Add your ore features
        biomeBuilder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.ORE_SOUL_SAND_BLOB);
        biomeBuilder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.ORE_SOUL_SOIL_BLOB);

        // Add basic nether features but skip the large open cave generation
        DefaultBiomeFeatures.addNetherMineables(biomeBuilder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomeBuilder);

        return (new Biome.Builder())
                .precipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(2300955)
                        .skyColor(OverworldBiomeCreator.getSkyColor(2.0F))
                        .particleConfig(new BiomeParticleConfig(ParticleTypes.ASH, 0.015F))
                        .loopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
                        .moodSound(new BiomeMoodSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD,
                                6000, 8, 2.0F))
                        .additionsSound(new BiomeAdditionsSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS,
                                0.0111))
                        .music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY)).build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }
}