package net.nullcoil.soulscorch.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;
import net.nullcoil.soulscorch.entity.custom.RestlessEntity;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;
import net.nullcoil.soulscorch.entity.custom.SoulscorchFireballEntity;

public class ModEntities {
    private static final Identifier SOUL_CHARGE_ID = Identifier.of(Soulscorch.MOD_ID, "soul_charge");
    private static final Identifier BLAZT_ID = Identifier.of(Soulscorch.MOD_ID, "blazt");
    private static final Identifier SOULLESS_ID = Identifier.of(Soulscorch.MOD_ID, "soulless");
    private static final Identifier RESTLESS_ID = Identifier.of(Soulscorch.MOD_ID, "restless");

    public static final EntityType<SoulscorchFireballEntity> SOUL_CHARGE = Registry.register(Registries.ENTITY_TYPE,
            SOUL_CHARGE_ID, EntityType.Builder.<SoulscorchFireballEntity>create(SoulscorchFireballEntity::new, SpawnGroup.MISC)
                    .dimensions(0.75f,0.75f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SOUL_CHARGE_ID)));

    public static final EntityType<BlaztEntity> BLAZT = Registry.register(Registries.ENTITY_TYPE,
            BLAZT_ID, EntityType.Builder.create(BlaztEntity::new, SpawnGroup.MONSTER)
                    .dimensions(3f,3f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, BLAZT_ID)));

    public static final EntityType<SoullessEntity> SOULLESS = Registry.register(Registries.ENTITY_TYPE,
            SOULLESS_ID, EntityType.Builder.create(SoullessEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f,1.95f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, SOULLESS_ID)));

    public static final EntityType<RestlessEntity> RESTLESS = Registry.register(Registries.ENTITY_TYPE,
            RESTLESS_ID, EntityType.Builder.create(RestlessEntity::new, SpawnGroup.MONSTER)
                    .dimensions(2f,3f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, RESTLESS_ID)));

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Entities for " + Soulscorch.MOD_ID);
    }
}