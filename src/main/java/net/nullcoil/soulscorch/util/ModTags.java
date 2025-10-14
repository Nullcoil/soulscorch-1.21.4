package net.nullcoil.soulscorch.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SOULBASED_BLOCKS = createTag("soulbased_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Soulscorch.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> SOULBASED_ITEMS = createTag("soulbased_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Soulscorch.MOD_ID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SOULSCORCH_ENTITIES = createTag("soulscorch_entities");
        public static final TagKey<EntityType<?>> PHOBIAS_OF_PIGLINS = createTag("phobias_of_piglins");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Soulscorch.MOD_ID, name));
        }
    }
}
