package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.World;

public class BlaztEntity extends FlyingEntity implements Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;



    protected BlaztEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }
}
