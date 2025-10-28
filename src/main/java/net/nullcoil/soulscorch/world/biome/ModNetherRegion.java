package net.nullcoil.soulscorch.world.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModNetherRegion extends Region {

    public ModNetherRegion(Identifier name, int weight) {
        super(name, RegionType.NETHER, weight);
    }

    @Override
    public void addBiomes(net.minecraft.registry.Registry<Biome> registry,
                          Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {

        // Copy Soul Sand Valley parameters exactly
        mapper.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // temperature
                        MultiNoiseUtil.ParameterRange.of(-0.5F), // humidity
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // continentalness
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // erosion
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // depth
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // weirdness
                        0L                                        // offset as long
                ),
                ModBiomes.SOULVORE_CAVERNS
        ));
    }
}