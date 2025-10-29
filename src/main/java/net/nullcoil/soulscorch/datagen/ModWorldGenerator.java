package net.nullcoil.soulscorch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.nullcoil.soulscorch.world.gen.ModNoiseParameters;

import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {

    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        // Add all registered entries
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.BIOME));
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.PLACED_FEATURE));
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.CONFIGURED_CARVER)); // Make sure this is here

        // Add noise parameters for dense terrain
        entries.add(ModNoiseParameters.DENSE_TERRAIN,
                new DoublePerlinNoiseSampler.NoiseParameters(-5, 1.5, 1.0));
        entries.add(ModNoiseParameters.SOUL_SAND_BLOB,
                new DoublePerlinNoiseSampler.NoiseParameters(-7, 1.0, 1.0));
        entries.add(ModNoiseParameters.SOUL_SOIL_BLOB,
                new DoublePerlinNoiseSampler.NoiseParameters(-7, 1.0, 1.0));
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}