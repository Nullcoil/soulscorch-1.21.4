package net.nullcoil.soulscorch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;
import net.nullcoil.soulscorch.Soulscorch;

import java.util.concurrent.CompletableFuture;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {

    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        // This will automatically add all entries from our bootstrap registries
        // Add biomes
        entries.addAll(registries.getOrThrow(net.minecraft.registry.RegistryKeys.BIOME));
        // Add configured features
        entries.addAll(registries.getOrThrow(net.minecraft.registry.RegistryKeys.CONFIGURED_FEATURE));
        // Add placed features
        entries.addAll(registries.getOrThrow(net.minecraft.registry.RegistryKeys.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return "World Gen";
    }
}