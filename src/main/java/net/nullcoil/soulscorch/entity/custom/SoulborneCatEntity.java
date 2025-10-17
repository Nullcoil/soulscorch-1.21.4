package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.util.ModTags;

public class SoulborneCatEntity extends CatEntity {
    public SoulborneCatEntity(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isFireImmune() { return true; }

    @Override
    public void tick() {
        super.tick();

        if(!this.getWorld().isClient &&
                this.isTamed() &&
                !this.isSitting() &&
                this.getOwner() instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(ModEffects.CAT_BUFF,
                    20,
                    0,
                    false,
                    false,
                    true));
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        if(this.getWorld().getBiome(this.getBlockPos()).isIn(ModTags.Biomes.SOUL_BIOMES) && this.isTamed()) {
            return SoundEvents.ENTITY_CAT_PURR;
        }
        return super.getAmbientSound();
    }
}
