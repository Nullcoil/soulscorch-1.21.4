package net.nullcoil.soulscorch.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.block.ModBlocks;

public class ModBlockEntities {
    public static BlockEntityType<SoulBrewingStandBlockEntity> SOUL_BREWING_STAND = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Soulscorch.MOD_ID, "soul_brewing_stand"),
            FabricBlockEntityTypeBuilder.create(SoulBrewingStandBlockEntity::new, ModBlocks.SOUL_BREWING_STAND).build());

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Block Entities for " + Soulscorch.MOD_ID);
    }
}
