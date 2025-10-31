package net.nullcoil.soulscorch.world.gen.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.nullcoil.soulscorch.Soulscorch;

import java.util.List;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> ORE_SOUL_SAND_BLOB =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_sand_blob"));
    public static final RegistryKey<PlacedFeature> ORE_SOUL_SOIL_BLOB =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "ore_soul_soil_blob"));
    public static final RegistryKey<PlacedFeature> PATCH_SOUL_ZOL =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Soulscorch.MOD_ID, "patch_soul_zol"));

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // More frequent placement for the massive blobs
        register(context, ORE_SOUL_SAND_BLOB,
                configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.ORE_SOUL_SAND_BLOB),
                List.of(
                        CountPlacementModifier.of(16), // More frequent placement
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                net.minecraft.world.gen.YOffset.getBottom(),
                                net.minecraft.world.gen.YOffset.getTop()
                        ),
                        BiomePlacementModifier.of()
                ));

        // More frequent soul soil placement
        register(context, ORE_SOUL_SOIL_BLOB,
                configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.ORE_SOUL_SOIL_BLOB),
                List.of(
                        CountPlacementModifier.of(12), // More frequent placement
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                net.minecraft.world.gen.YOffset.getBottom(),
                                net.minecraft.world.gen.YOffset.getTop()
                        ),
                        BiomePlacementModifier.of()
                ));

        // Replace your Soul Zol placement with this:
        register(context, PATCH_SOUL_ZOL,
                configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.PATCH_SOUL_ZOL),
                List.of(
                        RarityFilterPlacementModifier.of(4), // 1 in 4 chunks gets patches
                        SquarePlacementModifier.of(),
                        HeightRangePlacementModifier.uniform(
                                YOffset.BOTTOM,
                                YOffset.TOP
                        ), // Only in the main nether range
                        SurfaceThresholdFilterPlacementModifier.of(
                                Heightmap.Type.WORLD_SURFACE_WG,
                                0, // Min air above
                                0  // Max air above (0 means must have solid block above)
                        ),
                        BlockFilterPlacementModifier.of(
                                BlockPredicate.allOf(
                                        BlockPredicate.IS_AIR_OR_WATER,
                                        BlockPredicate.hasSturdyFace(Direction.DOWN)
                                )
                        ),
                        BiomePlacementModifier.of()
                ));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                 net.minecraft.registry.entry.RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}