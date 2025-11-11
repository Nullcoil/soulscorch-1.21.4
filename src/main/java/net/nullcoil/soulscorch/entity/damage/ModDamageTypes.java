package net.nullcoil.soulscorch.entity.damage;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public interface ModDamageTypes {
    RegistryKey<DamageType> MANIA = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Soulscorch.MOD_ID, "mania"));

    static void bootstrap(Registerable<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(MANIA, new DamageType("mania", 0.1F, DamageEffects.HURT));
    }
}
