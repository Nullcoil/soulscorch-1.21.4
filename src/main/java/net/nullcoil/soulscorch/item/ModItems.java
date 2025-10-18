package net.nullcoil.soulscorch.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.ModEntities;

public class ModItems {
    public static final Item BLAZT_POWDER = registerItem("blazt_powder",new Item(new Item.Settings()
            .registryKey(Key("blazt_powder"))));
    public static final Item BLAZT_ROD = registerItem("blazt_rod",new Item(new Item.Settings()
            .registryKey(Key("blazt_rod"))));
    public static final Item SOUL_CREAM = registerItem("soul_cream",new Item(new Item.Settings()
            .registryKey(Key("soul_cream"))));
    public static final Item SOUL_CHARGE = registerItem("soul_charge",new Item(new Item.Settings()
            .registryKey(Key("soul_charge"))));
    public static final Item SOUL_SHARD=registerItem("soul_shard", new Item(new Item.Settings()
            .registryKey(Key("soul_shard"))));
    public static final Item SOULWARD_TOTEM = registerItem("soulward_totem", new SoulwardTotemItem(new Item.Settings()
            .rarity(Rarity.UNCOMMON)
            .registryKey(Key("soulward_totem"))));
    public static final Item BLAZT_SPAWN_EGG = registerItem(
            "blazt_spawn_egg", new SpawnEggItem(ModEntities.BLAZT,
            new Item.Settings().registryKey(Key("blazt_spawn_egg"))));
    public static final Item SOULLESS_SPAWN_EGG = registerItem(
            "soulless_spawn_egg", new SpawnEggItem(ModEntities.SOULLESS,
                    new Item.Settings().registryKey(Key("soulless_spawn_egg"))));
    public static final Item RESTLESS_SPAWN_EGG = registerItem(
            "restless_spawn_egg", new SpawnEggItem(ModEntities.RESTLESS,
                    new Item.Settings().registryKey(Key("restless_spawn_egg"))));
    public static final Item SOULBORNE_CAT_SPAWN_EGG = registerItem(
            "soulborne_cat_spawn_egg", new SpawnEggItem(ModEntities.SOULBORNE_CAT,
                    new Item.Settings().registryKey(Key("soulborne_cat_spawn_egg"))));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Soulscorch.MOD_ID, name), item);
    }

    private static RegistryKey<Item> Key(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID, path));
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Items for " + Soulscorch.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLAZT_POWDER);
            entries.add(BLAZT_ROD);
            entries.add(SOUL_CREAM);
            entries.add(SOUL_CHARGE);
            entries.add(SOUL_SHARD);
            entries.add(SOULWARD_TOTEM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(BLAZT_SPAWN_EGG);
            entries.add(SOULLESS_SPAWN_EGG);
            entries.add(RESTLESS_SPAWN_EGG);
            entries.add(SOULBORNE_CAT_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(SOULWARD_TOTEM);
        });

    }
}
