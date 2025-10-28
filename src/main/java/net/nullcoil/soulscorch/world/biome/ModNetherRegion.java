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

        // Use unique parameters that don't conflict with existing Nether biomes
        // Let's place it in a different temperature/humidity range than Soul Sand Valley
        mapper.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                        MultiNoiseUtil.ParameterRange.of(0.3F, 0.6F),  // temperature (different from Soul Sand Valley's 0.0)
                        MultiNoiseUtil.ParameterRange.of(-0.8F, -0.4F), // humidity
                        MultiNoiseUtil.ParameterRange.of(0.0F, 0.3F),   // continentalness
                        MultiNoiseUtil.ParameterRange.of(0.0F, 0.3F),   // erosion
                        MultiNoiseUtil.ParameterRange.of(0.0F, 1.0F),   // depth (full range)
                        MultiNoiseUtil.ParameterRange.of(0.0F, 0.3F),   // weirdness
                        0L                                              // offset
                ),
                ModBiomes.SOULVORE_CAVERNS
        ));
    }
}