package net.nullcoil.soulscorch.world.biome.noise;

import net.minecraft.registry.Registerable;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.nullcoil.soulscorch.world.gen.feature.ModNoiseParameters;

public class ModNoiseBootstrap {
    public static void bootstrap(Registerable<DoublePerlinNoiseSampler.NoiseParameters> context) {
        context.register(ModNoiseParameters.SOUL_SAND_BLOB,
                new DoublePerlinNoiseSampler.NoiseParameters(-7, 1.0, 1.0)); // Use the same values as in data gen
        context.register(ModNoiseParameters.SOUL_SOIL_BLOB,
                new DoublePerlinNoiseSampler.NoiseParameters(-7, 1.0, 1.0)); // Use the same values as in data gen
        context.register(ModNoiseParameters.DENSE_TERRAIN,
                new DoublePerlinNoiseSampler.NoiseParameters(-5, 1.5, 1.0)); // Add this line
    }
}