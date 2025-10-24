package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HytodomEntity extends MobEntity {
    public final AnimationState IDLE = new AnimationState();


    public HytodomEntity(EntityType<? extends HytodomEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 2;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 @Nullable EntityData entityData) {
        EntityData data = super.initialize(world,difficulty,spawnReason, entityData);

        float yaw = this.random.nextFloat() * 360f - 180f;
        this.setYaw(yaw);
        this.setHeadYaw(yaw);
        this.setBodyYaw(yaw);
        this.setBaby(false);

        return data;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 8.0D)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.01);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getWorld().isClient) {
            this.IDLE.startIfNotRunning(this.age);
        }
    }
}
