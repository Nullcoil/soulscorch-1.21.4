package net.nullcoil.soulscorch.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModBlocks {

    public static final Block SOUL_BREWING_STAND = registerBlock("soul_brewing_stand",
            new SoulBrewingStandBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(0.5F)
                    .luminance(state -> 1)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Soulscorch.MOD_ID, "soul_brewing_stand")))));
    public static final Block SOUL_SLAG_BLOCK = registerBlock(
            "soul_slag",
            new SoulSlagBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .luminance((state) -> 2)
                    .strength(0.5F)
                    .allowsSpawning((state, world, pos, entityType) -> entityType
                            .isFireImmune())
                    .postProcess(Blocks::always)
                    .emissiveLighting(Blocks::always)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Soulscorch.MOD_ID, "soul_slag")))));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Soulscorch.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Soulscorch.MOD_ID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID, name)))));
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Blocks for " + Soulscorch.MOD_ID);

        // Add to item group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(SOUL_BREWING_STAND);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(SOUL_SLAG_BLOCK);
        });
    }
}