package net.nullcoil.soulscorch.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModItems {
    public static final Item BLAZT_POWDER = registerItem("pink_garnet",new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID,"blazt_powder")))));
    public static final Item BLAZT_ROD = registerItem("blazt_rod",new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID,"blazt_rod")))));
    public static final Item SOUL_CREAM = registerItem("soul_cream",new Item(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID,"soul_cream")))));;

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Soulscorch.MOD_ID, name), item);
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Items for " + Soulscorch.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLAZT_POWDER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLAZT_ROD);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(SOUL_CREAM);
        });
    }
}
