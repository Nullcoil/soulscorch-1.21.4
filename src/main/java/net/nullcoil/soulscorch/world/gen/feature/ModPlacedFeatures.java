package net.nullcoil.soulscorch.world.gen.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
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
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                 net.minecraft.registry.entry.RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}