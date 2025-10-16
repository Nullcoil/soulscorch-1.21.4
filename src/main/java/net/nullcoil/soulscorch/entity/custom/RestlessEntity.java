package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.sound.ModSounds;
import net.nullcoil.soulscorch.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RestlessEntity extends HostileEntity implements Monster, Hoglin {
    private static final TrackedData<Boolean> AWAKENED;
    private static Boolean aiming = false;
    private Vec3d chargeDirection;

    public RestlessEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
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

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AWAKENED, false);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return ZoglinEntity.createZoglinAttributes()
                .add(EntityAttributes.ATTACK_DAMAGE, 7.0f)
                .add(EntityAttributes.STEP_HEIGHT, 1f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.6);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(7, new LookAtTargetGoal(this));
        this.goalSelector.add(0, new BullrushGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public boolean getAwakened() { return this.dataTracker.get(AWAKENED); }

    public void setAwakened(Boolean awakened) {
        this.dataTracker.set(AWAKENED, awakened);
        if(awakened) playSound(ModSounds.RESTLESS_ANGER);
    }

    @Override
    public boolean isFireImmune() { return true; }

    @Override
    public SoundCategory getSoundCategory() { return SoundCategory.HOSTILE; }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!getAwakened()) return SoundEvents.PARTICLE_SOUL_ESCAPE.value();

        return SoundEvents.BLOCK_FIRE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return ModSounds.SOULLESS_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return ModSounds.RESTLESS_DEATH; }

    static { AWAKENED = DataTracker.registerData(RestlessEntity.class, TrackedDataHandlerRegistry.BOOLEAN); }

    @Override
    public int getMovementCooldownTicks() {
        return 0;
    }

    public static class InterceptHelper {
        public static Vec3d computeIntercept(Vec3d r0, Vec3d p0, Vec3d v, double s) {
            Vec3d d = p0.subtract(r0);
            double dv = d.dotProduct(v);
            double vv = v.dotProduct(v);
            double dd = d.dotProduct(d);

            double A = vv - s * s;
            double B = 2 * dv;
            double C = dd;

            double disc = B * B - 4 * A * C;
            if (disc < 0 || Math.abs(A) < 1e-6) {
                return p0.add(v.multiply(70)); // fallback linear prediction
            }

            double sqrtDisc = Math.sqrt(disc);
            double t1 = (-B - sqrtDisc) / (2 * A);
            double t2 = (-B + sqrtDisc) / (2 * A);

            double t = Double.MAX_VALUE;
            if (t1 > 0) t = t1;
            if (t2 > 0 && t2 < t) t = t2;

            if (t == Double.MAX_VALUE) {
                return p0.add(v.multiply(70));
            }

            return p0.add(v.multiply(t));
        }
    }


    public class BullrushGoal extends Goal {
        private final RestlessEntity mob;
        private LivingEntity target;
        private Vec3d interceptPoint;
        private int timer;
        private enum State { WAITING, CHARGING, DONE }
        private State state = State.DONE;

        public BullrushGoal(RestlessEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            return mob.getAwakened() && mob.getTarget() != null;
        }

        @Override
        public void start() {
            target = mob.getTarget();
            timer = 60; // 3 seconds wait
            state = State.WAITING;
            interceptPoint = null;
        }

        @Override
        public void tick() {
            switch (state) {
                case WAITING:
                    aiming = true;
                    mob.getNavigation().stop();

                    // Face the target during the aiming phase
                    if (target != null && !target.isRemoved()) {
                        double dx = target.getX() - mob.getX();
                        double dz = target.getZ() - mob.getZ();
                        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                        mob.setYaw(yaw);
                        mob.setBodyYaw(yaw);
                        mob.setHeadYaw(yaw);
                    }

                    timer--;
                    if (timer <= 0) {
                        // Lock onto intercept position and calculate charge direction
                        Vec3d playerVel = target.getVelocity();
                        interceptPoint = InterceptHelper.computeIntercept(
                                mob.getPos(),
                                target.getPos(),
                                playerVel,
                                0.35 // charge speed, blocks per tick
                        );
                        // Calculate charge direction from current position to intercept point
                        Vec3d chargeDir = interceptPoint.subtract(mob.getPos()).normalize();
                        chargeDirection = chargeDir;

                        timer = 60; // 3 seconds charge
                        state = State.CHARGING;
                    }
                    break;

                case CHARGING:
                    aiming = false;

                    mob.move(MovementType.SELF, chargeDirection.multiply(mob.getAttributeValue(EntityAttributes.MOVEMENT_SPEED)));
                    mob.velocityModified = true;

                    mob.setYaw((float) Math.toDegrees(Math.atan2(-chargeDirection.x, chargeDirection.z)));
                    mob.setBodyYaw(mob.getYaw());
                    mob.setHeadYaw(mob.getYaw());

                    mob.getNavigation().stop();
                    mob.setTarget(null);

                    mob.getWorld().getOtherEntities(mob, mob.getBoundingBox().expand(0.5),
                            e -> e instanceof LivingEntity && e != mob).forEach(e -> {
                        LivingEntity living = (LivingEntity) e;
                        float strength = 1.5f;
                        double dx = living.getX() - mob.getX();
                        double dz = living.getZ() - mob.getZ();
                        double dist = Math.sqrt(dx * dx + dz * dz);
                        if (dist != 0) {
                            dx /= dist;
                            dz /= dist;

                            if (!living.getType().isIn(ModTags.Entities.SOULSCORCH_ENTITIES)) {
                                living.addVelocity(dx * strength, 0.4 * strength, dz * strength);
                                living.velocityModified = true;
                                living.damage(
                                        (ServerWorld) mob.getWorld(),
                                        mob.getDamageSources().mobAttack(mob),
                                        (float) mob.getAttributeValue(EntityAttributes.ATTACK_DAMAGE)
                                );
                                living.addStatusEffect(new StatusEffectInstance(
                                        ModEffects.SOULSCORCH,
                                        600,
                                        0,
                                        false,
                                        false,
                                        true
                                ));
                            }
                        }
                    });

                    timer--;
                    if (timer <= 0) {
                        state = State.DONE;
                        mob.setAwakened(false);
                        chargeDirection = null;
                    }
                    break;

                case DONE:
                    mob.setAwakened(false);
                    chargeDirection = null;
                    break;
            }
        }

        @Override
        public boolean shouldContinue() {
            return state != State.DONE;
        }
    }

    static class LookAtTargetGoal extends Goal {
        private final RestlessEntity restless;

        public LookAtTargetGoal(RestlessEntity restless) {
            this.restless = restless;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return restless.getAwakened() && aiming;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.restless.getTarget() == null) {
                Vec3d vec3d = this.restless.getVelocity();
                this.restless.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float) Math.PI));
                this.restless.bodyYaw = this.restless.getYaw();
            } else {
                LivingEntity livingEntity = this.restless.getTarget();
                if (livingEntity.squaredDistanceTo(this.restless) < 4096.0D) {
                    double e = livingEntity.getX() - this.restless.getX();
                    double f = livingEntity.getZ() - this.restless.getZ();
                    this.restless.setYaw(-((float) MathHelper.atan2(e, f)) * (180F / (float) Math.PI));
                    this.restless.bodyYaw = this.restless.getYaw();
                }
            }
        }
    }

}