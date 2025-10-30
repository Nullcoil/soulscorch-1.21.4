package net.nullcoil.soulscorch.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.block.ModBlocks;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_SOUL_SAND_BLOB =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_sand_blob"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_SOUL_SOIL_BLOB =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_soil_blob"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> SOULVORE_CAVERNS_VEGETATION_BONEMEAL = ConfiguredFeatures.of("soulvore_caverns_vegetation_bonemeal");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        // MASSIVE Soul Sand blobs - like ancient debris but bigger
        register(context, ORE_SOUL_SAND_BLOB, Feature.ORE,
                new OreFeatureConfig(
                        new BlockMatchRuleTest(ModBlocks.SOUL_STONE),
                        net.minecraft.block.Blocks.SOUL_SAND.getDefaultState(),
                        64, // HUGE size - these will be massive veins
                        0.1f // Very low discard chance for continuous veins
                ));

        // MASSIVE Soul Soil blobs
        register(context, ORE_SOUL_SOIL_BLOB, Feature.ORE,
                new OreFeatureConfig(
                        new BlockMatchRuleTest(ModBlocks.SOUL_STONE),
                        net.minecraft.block.Blocks.SOUL_SOIL.getDefaultState(),
                        52, // HUGE size
                        0.08f // Even lower discard chance
                ));


        WeightedBlockStateProvider weightedBlockStateProvider = new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                .add(Blocks.CRIMSON_ROOTS.getDefaultState(), 87)
                .add(Blocks.CRIMSON_FUNGUS.getDefaultState(), 11)
                .add(Blocks.WARPED_FUNGUS.getDefaultState(), 1)
                .build());
        ConfiguredFeatures.register(context, SOULVORE_CAVERNS_VEGETATION_BONEMEAL, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationFeatureConfig(weightedBlockStateProvider, 3, 1));
    }

    private static <FC extends OreFeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}