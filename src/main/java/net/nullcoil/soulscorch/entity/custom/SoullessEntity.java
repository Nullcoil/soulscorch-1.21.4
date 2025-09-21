package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessActivity;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessAnimations;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Random;

public class SoullessEntity extends ZombifiedPiglinEntity implements Monster {
    /*----ATTRIBUTES & STATS----*/
    private static final TrackedData<Integer> ACTIVITY =
            DataTracker.registerData(SoullessEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public final AnimationState passiveAnimationState = new AnimationState();
    public final AnimationState neutralAnimationState = new AnimationState();
    public final AnimationState hostileAnimationState = new AnimationState();

    private int ticksUntilNextNeutral = 0;
    private static final Random RANDOM = new Random();

    public SoullessEntity(EntityType<? extends ZombifiedPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            // Handle animation states based on current activity
            switch (getActivity()) {
                case PASSIVE -> {
                    passiveAnimationState.startIfNotRunning(this.age);
                    neutralAnimationState.stop();
                    hostileAnimationState.stop();
                }
                case NEUTRAL -> {
                    passiveAnimationState.stop();
                    hostileAnimationState.stop();

                    ticksUntilNextNeutral--;
                    if (ticksUntilNextNeutral <= 0) {
                        neutralAnimationState.start(this.age);
                        ticksUntilNextNeutral = 40 + RANDOM.nextInt(60); // Random interval between animations
                    }
                }
                case HOSTILE -> {
                    passiveAnimationState.stop();
                    neutralAnimationState.stop();
                    hostileAnimationState.startIfNotRunning(this.age);
                }
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ACTIVITY, SoullessActivity.PASSIVE.getId());
    }

    public SoullessActivity getActivity() {
        return SoullessActivity.values()[this.dataTracker.get(ACTIVITY)];
    }

    public void setActivity(@NotNull SoullessActivity activity) {
        this.dataTracker.set(ACTIVITY, activity.getId());
    }

    public void raiseActivity() {
        switch (getActivity()) {
            case PASSIVE -> setActivity(SoullessActivity.NEUTRAL);
            case NEUTRAL -> setActivity(SoullessActivity.HOSTILE);
            case HOSTILE -> {}
        }
    }

    public static DefaultAttributeContainer createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 15.0D)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0D).build();
    }

    /*----GOALS----*/

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LookAtTargetGoal(this) {
            @Override
            public boolean canStart() {
                return getActivity() != SoullessActivity.PASSIVE && getTarget() != null;
            }
        });
        this.goalSelector.add(2, new ZombieAttackGoal(this, (double)1.0F, false) {
            @Override
            public boolean canStart() {
                return getActivity() == SoullessActivity.HOSTILE;
            }
        });
        this.goalSelector.add(7, new WanderAroundFarGoal(this, (double)1.0F) {
            @Override
            public boolean canStart() {
                return getActivity() == SoullessActivity.HOSTILE;
            }
        });
        this.targetSelector.add(1, (new SoullessRevengeGoal(this, new Class[0])).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt) {
            @Override
            public boolean canStart() {
                return getActivity() == SoullessActivity.HOSTILE;
            }
        });
    }

    static class LookAtTargetGoal extends Goal {
        private final SoullessEntity soulless;

        public LookAtTargetGoal(SoullessEntity soulless) {
            this.soulless = soulless;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.soulless.getTarget() == null) {
                Vec3d vec3d = this.soulless.getVelocity();
                this.soulless.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float) Math.PI));
                this.soulless.bodyYaw = this.soulless.getYaw();
            } else {
                LivingEntity livingEntity = this.soulless.getTarget();
                if (livingEntity.squaredDistanceTo(this.soulless) < 4096.0D) {
                    double e = livingEntity.getX() - this.soulless.getX();
                    double f = livingEntity.getZ() - this.soulless.getZ();
                    this.soulless.setYaw(-((float) MathHelper.atan2(e, f)) * (180F / (float) Math.PI));
                    this.soulless.bodyYaw = this.soulless.getYaw();
                }
            }
        }
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

    /*----SOUNDS----*/

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


}