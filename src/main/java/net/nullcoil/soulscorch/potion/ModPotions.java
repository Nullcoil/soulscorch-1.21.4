package net.nullcoil.soulscorch.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.effect.ModEffects;

public class ModPotions {
    public static final RegistryEntry<Potion> SOUL_RENDER_POTION = registerPotion("soul_render_potion",
            new Potion("soul_render", new StatusEffectInstance(ModEffects.SOUL_RENDER, 1, 0)));


    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(Soulscorch.MOD_ID, name), potion);
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Potions for " + Soulscorch.MOD_ID);
    }
}
