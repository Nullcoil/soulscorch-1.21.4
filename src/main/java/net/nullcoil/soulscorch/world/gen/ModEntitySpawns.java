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

public class ModEntitySpawns {
    public static void addSpawns() {
        Soulscorch.LOGGER.info("Adding Mod Entity Spawns for " + Soulscorch.MOD_ID);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SOUL_SAND_VALLEY),
                SpawnGroup.MONSTER, ModEntities.BLAZT, 15, 1, 1);
        SpawnRestriction.register(ModEntities.BLAZT, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SOUL_SAND_VALLEY),
                SpawnGroup.MONSTER, ModEntities.SOULLESS, 50, 2, 8);
        SpawnRestriction.register(ModEntities.SOULLESS,SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING, MobEntity::canMobSpawn);
    }
}
