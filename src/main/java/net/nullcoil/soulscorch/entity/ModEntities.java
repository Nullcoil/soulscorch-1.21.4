package net.nullcoil.soulscorch.entity;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;
import net.nullcoil.soulscorch.entity.custom.SoulscorchFireballEntity;

public class ModEntities {
    public static final EntityType<SoulscorchFireballEntity> SOUL_CHARGE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "soul_charge"),
            EntityType.Builder.<SoulscorchFireballEntity>create(SoulscorchFireballEntity::new, SpawnGroup.MISC)
                    .dimensions(0.75f,0.75f).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(Soulscorch.MOD_ID, "soul_charge"))));
    public static final EntityType<BlaztEntity> BLAZT = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "blazt"),
            EntityType.Builder.create(BlaztEntity::new, SpawnGroup.MONSTER)
                    .dimensions(3f,3f).build(
                            RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                                    Identifier.of(Soulscorch.MOD_ID, "blazt"))
                    ));

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Entities for " + Soulscorch.MOD_ID);
    }
}