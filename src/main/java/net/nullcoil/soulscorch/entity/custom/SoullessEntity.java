package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessActivity;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessAnimations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

public class SoullessEntity extends ZombifiedPiglinEntity implements Angerable {
    @Nullable
    private UUID angryAt;
    private int angerTime;
    private static final UniformIntProvider ANGER_TIME_RANGE;

    private static final TrackedData<Integer> ACTIVITY =
            DataTracker.registerData(SoullessEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState passiveAnimationState = new AnimationState();
    public final AnimationState neutralAnimationState = new AnimationState();
    public final AnimationState hostileAnimationState = new AnimationState();

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 @Nullable EntityData entityData) {
        EntityData data = super.initialize(world,difficulty,spawnReason, entityData);

        float yaw = this.random.nextFloat() * 360f - 180f;
        this.setYaw(yaw);
        this.setHeadYaw(yaw);
        this.setBodyYaw(yaw);

        return data;
    }

    private Animation currentAnimation;
    private int ticksUntilNext = 0;
    private int twitchDuration = 0;
    private static final Random RANDOM = new Random();

    public SoullessEntity(EntityType<? extends ZombifiedPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ACTIVITY, SoullessActivity.PASSIVE.getId());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return ZombifiedPiglinEntity.createZombifiedPiglinAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.FOLLOW_RANGE, 35.0);
    }

    public SoullessActivity getActivity() {
        return SoullessActivity.values()[this.dataTracker.get(ACTIVITY)];
    }

    public void setActivity(@NotNull SoullessActivity activity) {
        this.dataTracker.set(ACTIVITY, activity.getId());
        this.goalSelector.clear(goal -> true);
        this.targetSelector.clear(goal -> true);

        switch (activity) {
            case PASSIVE -> {}
            case NEUTRAL -> {
                this.goalSelector.add(7, new LookAtTargetGoal(this, 0));
                this.targetSelector.add(7, new ActiveTargetGoal(this, PlayerEntity.class,true));
            }
            case HOSTILE -> {
                this.goalSelector.add(2, new ZombieAttackGoal(this, (double)1.0F, false));
                this.goalSelector.add(6, new MoveThroughVillageGoal(this, (double)1.0F, true, 4, this::canBreakDoors));
                this.goalSelector.add(7, new WanderAroundFarGoal(this, (double)1.0F));
                this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[]{ZombifiedPiglinEntity.class}));
                this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
            }
        }
    }

    public void raiseActivity() {
        switch (getActivity()) {
            case PASSIVE -> setActivity(SoullessActivity.NEUTRAL);
            case NEUTRAL -> setActivity(SoullessActivity.HOSTILE);
            case HOSTILE -> {}
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (twitchDuration > 0) {
            twitchDuration--;
        }

        if (ticksUntilNext > 0) {
            ticksUntilNext--;
        } else {
            switch(getActivity()) {
                case PASSIVE -> currentAnimation = SoullessAnimations.PASSIVE;
                case NEUTRAL -> currentAnimation = SoullessAnimations.NEUTRAL();
                case HOSTILE -> {} // could add HOSTILE animation later
            }
            if (currentAnimation != null) {
                twitchDuration = (int)(currentAnimation.lengthInSeconds() * 20);
            }
            ticksUntilNext = 40; // + RANDOM.nextInt(20);
        }

        if (this.getWorld().isClient()) {
            passiveAnimationState.stop();
            neutralAnimationState.stop();
            hostileAnimationState.stop();

            switch (getActivity()) {
                case PASSIVE -> passiveAnimationState.startIfNotRunning(this.age);
                case NEUTRAL -> neutralAnimationState.startIfNotRunning(this.age);
                case HOSTILE -> hostileAnimationState.startIfNotRunning(this.age);
            }
        }
    }

    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean bl = super.tryAttack(world, target);
        if (bl && target instanceof LivingEntity) {
            float f = this.getWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(
                    ModEffects.SOULSCORCH,
                    600, // Duration in ticks (30 seconds)
                    0,   // Amplifier
                    false, // Show particles
                    true,  // Show icon
                    false
            ));
        }

        return bl;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        // Call the parent method first to actually apply damage
        boolean wasDamaged = super.damage(world, source, amount);

        if (wasDamaged && getActivity() != SoullessActivity.HOSTILE) {
            // Optional: Ignore certain damage types (like environmental)
            if (source.getAttacker() instanceof PlayerEntity) {
                this.setActivity(SoullessActivity.HOSTILE);
            }
        }

        return wasDamaged;
    }

    public Animation getCurrentAnimation() {
        return this.currentAnimation;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected void initGoals() {
        switch(this.getActivity()) {
            case PASSIVE -> {}
            case NEUTRAL -> {
                this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            }
            case HOSTILE -> {
                this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0F, false));
                this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0F));
                this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            }
        }
        this.targetSelector.add(1, (new SoullessRevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
    }

    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {
        ANGER_TIME_RANGE.get(this.random);
    }

    @Override
    public @Nullable UUID getAngryAt() {
        return null;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    static class SoullessRevengeGoal extends RevengeGoal {
        public SoullessRevengeGoal(SoullessEntity mob, Class<?>... noRevengeTypes) {
            super(mob, noRevengeTypes);
        }

        @Override
        public void start() {
            super.start();
            if (this.mob instanceof SoullessEntity soulless) {
                soulless.setActivity(SoullessActivity.HOSTILE);
            }
        }
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public SoundEvent getAmbientSound() {
        switch(getActivity()) {
            case NEUTRAL -> { return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT; }
            case HOSTILE -> { return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY; }
        }
        return SoundEvents.PARTICLE_SOUL_ESCAPE.value();
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {}

    static class LookAtTargetGoal extends ZombieAttackGoal {
        public LookAtTargetGoal(SoullessEntity soulless, double speed) {
            super(soulless, speed, false);
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true; // always run
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null && target.squaredDistanceTo(this.mob) < 4096.0D) {
                this.mob.getLookControl().lookAt(
                        target.getX(),
                        target.getEyeY(),
                        target.getZ(),
                        360.0F,
                        360.0F
                );
            }
            // do not call super.tick() if you don't want attacks
        }
    }


    static {
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    }
}