package net.nullcoil.soulscorch.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModEffects {
    public static final RegistryEntry<StatusEffect> SOUL_RENDER = registerStatusEffect("soul_render",
            new SoulRenderEffect(StatusEffectCategory.BENEFICIAL, 0x880000));

    public static final RegistryEntry<StatusEffect> SOULSCORCH = registerStatusEffect("soulscorch",
            new SoulscorchEffect(StatusEffectCategory.HARMFUL, 0x34ebd5));

    private static RegistryEntry<StatusEffect> registerStatusEffect (String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Soulscorch.MOD_ID, name), statusEffect);
    }

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Effects for " + Soulscorch.MOD_ID);
    }
}
