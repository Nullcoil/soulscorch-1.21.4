package net.nullcoil.soulscorch.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.core.time.Instant;
import org.jetbrains.annotations.Nullable;

public class SoulRenderEffect extends InstantStatusEffect {
    protected SoulRenderEffect(StatusEffectCategory category, int color) { super(category, color); }

    @Override
    public void applyInstantEffect(ServerWorld world, @Nullable Entity source, @Nullable Entity attacker,
                                   LivingEntity target, int amplifier, double proximity) {
        EntityAttributeInstance maxHealthAttr = target.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(20.0D);
            }
    }
}

