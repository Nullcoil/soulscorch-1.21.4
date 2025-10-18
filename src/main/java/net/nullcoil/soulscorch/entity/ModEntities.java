package net.nullcoil.soulscorch.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.*;

public class ModEntities {
    private static final Identifier SOUL_CHARGE_ID = ID( "soul_charge");
    private static final Identifier BLAZT_ID = ID("blazt");
    private static final Identifier SOULLESS_ID = ID("soulless");
    private static final Identifier RESTLESS_ID = ID("restless");
    private static final Identifier SOULCAT_ID = ID("soulborne_cat");

    public static final EntityType<SoulscorchFireballEntity> SOUL_CHARGE = Registry.register(Registries.ENTITY_TYPE,
            SOUL_CHARGE_ID, EntityType.Builder.<SoulscorchFireballEntity>create(SoulscorchFireballEntity::new, SpawnGroup.MISC)
                    .dimensions(0.75f,0.75f)
                    .build(Key(SOUL_CHARGE_ID)));

    public static final EntityType<BlaztEntity> BLAZT = Registry.register(Registries.ENTITY_TYPE,
            BLAZT_ID, EntityType.Builder.create(BlaztEntity::new, SpawnGroup.MONSTER)
                    .dimensions(3f,3f)
                    .build(Key(BLAZT_ID)));

    public static final EntityType<SoullessEntity> SOULLESS = Registry.register(Registries.ENTITY_TYPE,
            SOULLESS_ID, EntityType.Builder.create(SoullessEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f,1.95f)
                    .build(Key(SOULLESS_ID)));

    public static final EntityType<RestlessEntity> RESTLESS = Registry.register(Registries.ENTITY_TYPE,
            RESTLESS_ID, EntityType.Builder.create(RestlessEntity::new, SpawnGroup.MONSTER)
                    .dimensions(1.6f,1.4f)
                    .build(Key(RESTLESS_ID)));

    public static final EntityType<SoulborneCatEntity> SOULBORNE_CAT = Registry.register(Registries.ENTITY_TYPE,
            SOULCAT_ID, EntityType.Builder.create(SoulborneCatEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.5f,0.5f)
                    .build(Key(SOULCAT_ID)));

    private static Identifier ID(String path) {
        return Identifier.of(Soulscorch.MOD_ID, path);
    }

    private static RegistryKey<EntityType<?>> Key(Identifier ID) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, ID);
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Entities for " + Soulscorch.MOD_ID);

        FabricDefaultAttributeRegistry.register(BLAZT, BlaztEntity.createBlaztAttributes());
        FabricDefaultAttributeRegistry.register(SOULLESS, SoullessEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RESTLESS, RestlessEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SOULBORNE_CAT, SoulborneCatEntity.createCatAttributes());
    }
}