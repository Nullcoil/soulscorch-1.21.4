package net.nullcoil.soulscorch.entity.custom;

import java.util.EnumSet;


import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlaztEntity extends FlyingEntity implements Monster {
    private static final TrackedData<Boolean> SHOOTING;
    private int fireballStrength = 1;

    // Animation states
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    public BlaztEntity(EntityType<? extends BlaztEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new BlaztMoveControl(this);
    }



    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new FlyRandomlyGoal(this));
        this.goalSelector.add(7, new LookAtTargetGoal(this));
        this.goalSelector.add(7, new ShootFireballGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                (entity, world) -> Math.abs(entity.getY() - this.getY()) <= 4.0F));
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    // === Animation logic ===
    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            // Always ensure idle animation is running
            this.idleAnimationState.startIfNotRunning(this.age);

            // Shooting animation management
            if (this.isShooting()) {
                this.shootAnimationState.startIfNotRunning(this.age);
            } else {
                this.shootAnimationState.stop();
            }
        }
        if (this.getWorld().isClient()) {
            // Always ensure idle animation is running
            this.idleAnimationState.startIfNotRunning(this.age);

            // Shooting animation management
            if (this.isShooting()) {
                this.shootAnimationState.startIfNotRunning(this.age);
            } else {
                this.shootAnimationState.stop();
            }

            // Spawn smoke particles continuously (like a blaze)
            this.spawnSmokeParticles();
        }
    }

    // === Shooting tracker ===
    public boolean isShooting() {
        return this.dataTracker.get(SHOOTING);
    }

    public void setShooting(boolean shooting) {
        this.dataTracker.set(SHOOTING, shooting);
    }

    public int getFireballStrength() {
        return this.fireballStrength;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    private static boolean isFireballFromPlayer(DamageSource damageSource) {
        return damageSource.getSource() instanceof FireballEntity && damageSource.getAttacker() instanceof PlayerEntity;
    }

    @Override
    public boolean isInvulnerableTo(ServerWorld world, DamageSource source) {
        return (this.isInvulnerable() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))
                || (!isFireballFromPlayer(source) && super.isInvulnerableTo(world, source));
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (isFireballFromPlayer(source)) {
            super.damage(world, source, 1000.0F);
            return true;
        } else {
            return this.isInvulnerableTo(world, source) ? false : super.damage(world, source, amount);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SHOOTING, false);
    }

    public static DefaultAttributeContainer.Builder createBlaztAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 15.0D)
                .add(EntityAttributes.FOLLOW_RANGE, 100.0D);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    public static boolean canSpawn(EntityType<BlaztEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos,
                                   Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && random.nextInt(20) == 0
                && canMobSpawn(type, world, spawnReason, pos, random);
    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("ExplosionPower", (byte) this.fireballStrength);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ExplosionPower", 99)) {
            this.fireballStrength = nbt.getByte("ExplosionPower");
        }
    }

    static {
        SHOOTING = DataTracker.registerData(BlaztEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    // === Custom movement/AI ===
    static class BlaztMoveControl extends MoveControl {
        private final BlaztEntity blazt;
        private int collisionCheckCooldown;

        public BlaztMoveControl(BlaztEntity blazt) {
            super(blazt);
            this.blazt = blazt;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.blazt.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.blazt.getX(),
                            this.targetY - this.blazt.getY(),
                            this.targetZ - this.blazt.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.blazt.setVelocity(this.blazt.getVelocity().add(vec3d.multiply(0.1)));
                    } else {
                        this.state = State.WAIT;
                    }
                }
            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.blazt.getBoundingBox();
            for (int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.blazt.getWorld().isSpaceEmpty(this.blazt, box)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class FlyRandomlyGoal extends Goal {
        private final BlaztEntity blazt;

        public FlyRandomlyGoal(BlaztEntity blazt) {
            this.blazt = blazt;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            MoveControl moveControl = this.blazt.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.blazt.getX();
                double e = moveControl.getTargetY() - this.blazt.getY();
                double f = moveControl.getTargetZ() - this.blazt.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0D || g > 3600.0D;
            }
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.blazt.getRandom();
            double d = this.blazt.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double e = this.blazt.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double f = this.blazt.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            this.blazt.getMoveControl().moveTo(d, e, f, 1.0D);
        }
    }

    static class LookAtTargetGoal extends Goal {
        private final BlaztEntity blazt;

        public LookAtTargetGoal(BlaztEntity blazt) {
            this.blazt = blazt;
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
            if (this.blazt.getTarget() == null) {
                Vec3d vec3d = this.blazt.getVelocity();
                this.blazt.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float) Math.PI));
                this.blazt.bodyYaw = this.blazt.getYaw();
            } else {
                LivingEntity livingEntity = this.blazt.getTarget();
                if (livingEntity.squaredDistanceTo(this.blazt) < 4096.0D) {
                    double e = livingEntity.getX() - this.blazt.getX();
                    double f = livingEntity.getZ() - this.blazt.getZ();
                    this.blazt.setYaw(-((float) MathHelper.atan2(e, f)) * (180F / (float) Math.PI));
                    this.blazt.bodyYaw = this.blazt.getYaw();
                }
            }
        }
    }

    static class ShootFireballGoal extends Goal {
        private final BlaztEntity blazt;
        public int cooldown;

        public ShootFireballGoal(BlaztEntity blazt) {
            this.blazt = blazt;
        }

        @Override
        public boolean canStart() {
            return this.blazt.getTarget() != null;
        }

        @Override
        public void start() {
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            this.blazt.setShooting(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.blazt.getTarget();
            if (livingEntity != null) {
                if (livingEntity.squaredDistanceTo(this.blazt) < 4096.0D && this.blazt.canSee(livingEntity)) {
                    World world = this.blazt.getWorld();
                    ++this.cooldown;
                    if (this.cooldown == 10 && !this.blazt.isSilent()) {
                        world.syncWorldEvent(null, 1015, this.blazt.getBlockPos(), 0);
                    }

                    if (this.cooldown == 20) {
                        Vec3d vec3d = this.blazt.getRotationVec(1.0F);
                        double f = livingEntity.getX() - (this.blazt.getX() + vec3d.x * 4.0D);
                        double g = livingEntity.getBodyY(0.5D) - (0.5D + this.blazt.getBodyY(0.5D));
                        double h = livingEntity.getZ() - (this.blazt.getZ() + vec3d.z * 4.0D);
                        Vec3d vec3d2 = new Vec3d(f, g, h);
                        if (!this.blazt.isSilent()) {
                            world.syncWorldEvent(null, 1016, this.blazt.getBlockPos(), 0);
                        }

                        FireballEntity fireballEntity = new FireballEntity(world, this.blazt,
                                vec3d2.normalize(), this.blazt.getFireballStrength());
                        fireballEntity.setPosition(
                                this.blazt.getX() + vec3d.x * 4.0D,
                                this.blazt.getBodyY(0.5D) + 0.5D,
                                fireballEntity.getZ() + vec3d.z * 4.0D
                        );
                        world.spawnEntity(fireballEntity);
                        this.cooldown = -40;
                    }
                } else if (this.cooldown > 0) {
                    --this.cooldown;
                }
                this.blazt.setShooting(this.cooldown > 10);


            }
        }
    }

    private void spawnSmokeParticles() {
        if (this.getWorld().isClient()) {
            double centerX = this.getX();
            double centerY = this.getY() + 1;
            double centerZ = this.getZ();

            Random random = this.getRandom();

            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                double offsetX = (random.nextDouble() - 0.5);
                double offsetZ = (random.nextDouble() - 0.5);

                double velocityX = (random.nextDouble() - 0.5) * 0.02;
                double velocityY = -0.2 - random.nextDouble() * 0.05;
                double velocityZ = (random.nextDouble() - 0.5) * 0.02;

                this.getWorld().addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        centerX + offsetX,
                        centerY,
                        centerZ + offsetZ,
                        velocityX,
                        velocityY,
                        velocityZ
                );
            }
        }


    }
}




