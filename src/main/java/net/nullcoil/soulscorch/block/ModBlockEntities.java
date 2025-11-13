package net.nullcoil.soulscorch.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.nurvis.blockentity.NurvisConduitBlockEntity;
import net.nullcoil.soulscorch.nurvis.blockentity.organ.GlarynxBlockEntity;
import net.nullcoil.soulscorch.block.entity.SoulBrewingStandBlockEntity;

public class ModBlockEntities {
    public static BlockEntityType<SoulBrewingStandBlockEntity> SOUL_BREWING_STAND = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "soul_brewing_stand"),
            FabricBlockEntityTypeBuilder.create(SoulBrewingStandBlockEntity::new,
                    ModBlocks.SOUL_BREWING_STAND).build());
    /*
    public static BlockEntityType<ResonatingHeartBlockEntity> RESONATING_HEART = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "resonating_heart"),
            FabricBlockEntityTypeBuilder.create(ResonatingHeartBlockEntity::new,
                                                ModBlocks.RESONATING_HEART).build());
     */
    
    public static BlockEntityType<GlarynxBlockEntity> GLARYNX = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "glarynx"),
            FabricBlockEntityTypeBuilder.create(GlarynxBlockEntity::new,
                    ModBlocks.GLARYNX).build());

    public static BlockEntityType<NurvisConduitBlockEntity> NURVIS_CONDUIT = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "nurvis_conduit"),
            FabricBlockEntityTypeBuilder.create(NurvisConduitBlockEntity::new,
                    ModBlocks.NURVIS_CONDUIT).build());

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Block Entities for " + Soulscorch.MOD_ID);
    }
}
