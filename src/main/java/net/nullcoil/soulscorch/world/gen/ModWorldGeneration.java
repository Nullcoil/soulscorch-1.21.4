package net.nullcoil.soulscorch.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.SpawnSettings;

public class ModWorldGeneration {
    private static final Identifier ID = Identifier.of("soulscorch", "ghast_spawn_tweak");

    public static void generateModWorldGen() {
        RegistryKey<?> soulSandKey = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft", "soul_sand_valley"));
        BiomeModifications.create(ID).add(
                ModificationPhase.REPLACEMENTS,
                BiomeSelectors.includeByKey((RegistryKey) soulSandKey),
                ((biomeSelectionContext, biomeModificationContext) -> {
                    var spawnCtx = biomeModificationContext.getSpawnSettings();

                    spawnCtx.removeSpawnsOfEntityType(EntityType.GHAST);
                    spawnCtx.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GHAST, 35, 1, 4));
                })
        );

        ModEntitySpawns.addSpawns();
    }
}
