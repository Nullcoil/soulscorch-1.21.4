package net.nullcoil.soulscorch.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModWorldGeneration {
    private static final Identifier ID = Identifier.of("soulscorch", "ghast_spawn_tweak");

    public static void generateModWorldGen() {
        RegistryKey<?> soulSandKey = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft", "soul_sand_valley"));
        BiomeModifications.create(ID).add(
                ModificationPhase.REPLACEMENTS,
                BiomeSelectors.includeByKey((RegistryKey) soulSandKey),
                ((biomeSelectionContext, biomeModificationContext) -> {
                    var spawnCtx = biomeModificationContext.getSpawnSettings();
                })
        );

        ModEntitySpawns.addSpawns();
    }
}
