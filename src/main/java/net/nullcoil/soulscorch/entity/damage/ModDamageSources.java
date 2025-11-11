package net.nullcoil.soulscorch.entity.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;

public class ModDamageSources extends DamageSources{
    public final Registry<DamageType> registry;
    public final DamageSource mania;

    public ModDamageSources(DynamicRegistryManager registryManager) {
        super(registryManager);
        this.registry = registryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE);
        this.mania = this.create(ModDamageTypes.MANIA);
    }

    public DamageSource mania() {
        return this.mania;
    }
}
