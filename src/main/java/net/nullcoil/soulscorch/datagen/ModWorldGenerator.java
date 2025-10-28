package net.nullcoil.soulscorch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {

    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        // Cast to Impl<T> to satisfy FabricDynamicRegistryProvider
        Impl<Biome> biomeWrapper = wrapperLookup.getOrThrow(RegistryKeys.BIOME);

        // Add all biomes to the datagen provider
        entries.addAll(biomeWrapper);
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}
