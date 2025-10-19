package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.util.ModTags;
import org.jetbrains.annotations.Nullable;

public class SoulborneCatEntity extends CatEntity {
    private static final TrackedData<Boolean> IN_SLEEPING_POSE;
    private static final TrackedData<Boolean> HEAD_DOWN;
    private static final TrackedData<Integer> COLLAR_COLOR;

    public SoulborneCatEntity(EntityType<? extends CatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IN_SLEEPING_POSE, false);
        builder.add(HEAD_DOWN, false);
        builder.add(COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    protected float getVelocityMultiplier() {
        var blockState = this.getWorld().getBlockState(this.getVelocityAffectingPos());
        if (blockState.isIn(ModTags.Blocks.SOULBASED_BLOCKS)) { return 1.2F; }
        return super.getVelocityMultiplier();
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
            if(player.hasStatusEffect(ModEffects.SOULSCORCH)) player.removeStatusEffect(ModEffects.SOULSCORCH);
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        if(this.getWorld().getBiome(this.getBlockPos()).isIn(ModTags.Biomes.SOUL_BIOMES) && this.isTamed()) {
            return SoundEvents.ENTITY_CAT_PURR;
        }
        return super.getAmbientSound();
    }

    private void setCollarColor(DyeColor color) {
        this.dataTracker.set(COLLAR_COLOR, color.getId());
    }

    @Override
    @Nullable
    public CatEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        CatEntity catEntity = (CatEntity) ModEntities.SOULBORNE_CAT.create(serverWorld, SpawnReason.BREEDING);
        if (catEntity != null && passiveEntity instanceof CatEntity catEntity2) {
            if (this.random.nextBoolean()) {
                catEntity.setVariant(this.getVariant());
            } else {
                catEntity.setVariant(catEntity2.getVariant());
            }

            if (this.isTamed()) {
                catEntity.setOwnerUuid(this.getOwnerUuid());
                catEntity.setTamed(true, true);
                DyeColor dyeColor = this.getCollarColor();
                DyeColor dyeColor2 = catEntity2.getCollarColor();
                setCollarColor(DyeColor.mixColors(serverWorld, dyeColor, dyeColor2));
            }
        }

        return catEntity;
    }

    static {
        IN_SLEEPING_POSE = DataTracker.registerData(SoulborneCatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        HEAD_DOWN = DataTracker.registerData(SoulborneCatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        COLLAR_COLOR = DataTracker.registerData(SoulborneCatEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
