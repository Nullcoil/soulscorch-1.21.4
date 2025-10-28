package net.nullcoil.soulscorch;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.nullcoil.soulscorch.datagen.ModWorldGenerator;
import net.nullcoil.soulscorch.world.biome.ModBiomes;

import java.io.IOException;

public class SoulscorchDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(ModWorldGenerator::new);
        try {
            generator.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomes::bootstrap);
    }
}
