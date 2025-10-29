package net.nullcoil.soulscorch.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.custom.HytodomEntity;
import net.nullcoil.soulscorch.entity.custom.jellyfish.JellyfishEntity;
import net.nullcoil.soulscorch.world.biome.ModBiomes;

public class ModEntitySpawns {
    public static void addSpawns() {
        Soulscorch.LOGGER.info("Adding Mod Entity Spawns for " + Soulscorch.MOD_ID);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SOUL_SAND_VALLEY),
                SpawnGroup.MONSTER, ModEntities.BLAZT, 8, 1, 1);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SOUL_SAND_VALLEY),
                SpawnGroup.MONSTER, ModEntities.SOULLESS, 100, 2, 8);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SOUL_SAND_VALLEY),
                SpawnGroup.MONSTER, ModEntities.RESTLESS, 20, 1, 1);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.SOULVORE_CAVERNS),
                SpawnGroup.MONSTER, ModEntities.BLAZT, 1, 1, 1);
        SpawnRestriction.register(ModEntities.BLAZT, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.SOULVORE_CAVERNS),
                SpawnGroup.MONSTER, ModEntities.SOULLESS, 50, 2, 8);
        SpawnRestriction.register(ModEntities.SOULLESS,SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING, MobEntity::canMobSpawn);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.SOULVORE_CAVERNS),
                SpawnGroup.MONSTER, ModEntities.RESTLESS, 10, 1, 1);
        SpawnRestriction.register(ModEntities.RESTLESS, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING, MobEntity::canMobSpawn);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(ModBiomes.SOULVORE_CAVERNS),
                SpawnGroup.MONSTER, ModEntities.HYTODOM, 60, 2, 5);
        SpawnRestriction.register(ModEntities.HYTODOM, SpawnLocationTypes.UNRESTRICTED,
                Heightmap.Type.MOTION_BLOCKING, JellyfishEntity::canSpawn);
    }
}
