package net.nullcoil.soulscorch.item;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class SoulwardTotemItem extends Item {

    public SoulwardTotemItem(Settings settings) {
        super(settings.maxDamage(100).repairable(ModItems.SOUL_SHARD));
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return enchantment.matchesKey(Enchantments.UNBREAKING) ||
                enchantment.matchesKey(Enchantments.MENDING);
    }
}