package net.nullcoil.soulscorch.world.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
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

        // Use the EXACT same parameters as Soul Sand Valley but as a replacement
        // This will make our biome spawn in place of Soul Sand Valleys
        mapper.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // temperature (same as Soul Sand Valley)
                        MultiNoiseUtil.ParameterRange.of(-0.5F), // humidity (same as Soul Sand Valley)
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // continentalness (same as Soul Sand Valley)
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // erosion (same as Soul Sand Valley)
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // depth (same as Soul Sand Valley)
                        MultiNoiseUtil.ParameterRange.of(0.0F),  // weirdness (same as Soul Sand Valley)
                        0L                                       // offset (same as Soul Sand Valley)
                ),
                ModBiomes.SOULVORE_CAVERNS
        ));

        // Optional: Also replace some of the surrounding areas to ensure good coverage
        mapper.accept(Pair.of(
                MultiNoiseUtil.createNoiseHypercube(
                        MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F),   // temperature range around Soul Sand Valley
                        MultiNoiseUtil.ParameterRange.of(-0.6F, -0.4F),  // humidity range around Soul Sand Valley
                        MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F),   // continentalness range
                        MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F),   // erosion range
                        MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F),   // depth range
                        MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F),   // weirdness range
                        1L                                               // different offset
                ),
                ModBiomes.SOULVORE_CAVERNS
        ));
    }
}