package net.nullcoil.soulscorch.world.gen;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.nullcoil.soulscorch.Soulscorch;

import java.util.List;

public class ModNoiseParameters {
    public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> SOUL_SAND_BLOB =
            RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, Identifier.of(Soulscorch.MOD_ID, "soul_sand_blobs"));
    public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> SOUL_SOIL_BLOB =
            RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, Identifier.of(Soulscorch.MOD_ID, "soul_soil_blobs"));

    // Custom noise for dense terrain
    public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> DENSE_TERRAIN =
            RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, Identifier.of(Soulscorch.MOD_ID, "dense_terrain"));
}