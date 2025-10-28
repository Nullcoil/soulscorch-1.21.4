package net.nullcoil.soulscorch.world.gen.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.block.ModBlocks;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_SOUL_SAND_BLOB =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_sand_blob"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_SOUL_SOIL_BLOB =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_soil_blob"));

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        // Soul Sand blobs - generates in Soul Stone like Blackstone in Netherrack
        register(context, ORE_SOUL_SAND_BLOB, Feature.ORE,
                new OreFeatureConfig(
                        new BlockMatchRuleTest(ModBlocks.SOUL_STONE), // target block
                        net.minecraft.block.Blocks.SOUL_SAND.getDefaultState(), // replacement
                        20, // size of the vein
                        0.5f // discard chance on air exposure
                ));

        // Soul Soil blobs
        register(context, ORE_SOUL_SOIL_BLOB, Feature.ORE,
                new OreFeatureConfig(
                        new BlockMatchRuleTest(ModBlocks.SOUL_STONE), // target block
                        net.minecraft.block.Blocks.SOUL_SOIL.getDefaultState(), // replacement
                        16, // slightly smaller than soul sand
                        0.4f // discard chance on air exposure
                ));
    }

    private static <FC extends OreFeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}