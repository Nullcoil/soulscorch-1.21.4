package net.nullcoil.soulscorch.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.sound.ModSounds;

public class RestlessEntity extends HostileEntity implements Monster, Hoglin {
    private static final TrackedData<Boolean> AWAKENED;
    public RestlessEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AWAKENED, false);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return ZoglinEntity.createZoglinAttributes()
                .add(EntityAttributes.ATTACK_DAMAGE, 7.0f)
                .add(EntityAttributes.STEP_HEIGHT, 1f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new BullrushGoal(this));
    }

    public boolean getAwakened() { return this.dataTracker.get(AWAKENED); }

    public void setAwakened(Boolean awakened) { this.dataTracker.set(AWAKENED, awakened); }

    @Override
    public boolean isFireImmune() { return true; }

    @Override
    public SoundCategory getSoundCategory() { return SoundCategory.HOSTILE; }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.PARTICLE_SOUL_ESCAPE.value(); }

    static { AWAKENED = DataTracker.registerData(RestlessEntity.class, TrackedDataHandlerRegistry.BOOLEAN); }

    @Override
    public int getMovementCooldownTicks() {
        return 0;
    }

    public class InterceptHelper {
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
                    mob.getNavigation().stop();
                    timer--;
                    if (timer <= 0) {
                        // Lock onto intercept position
                        Vec3d playerVel = target.getVelocity();
                        interceptPoint = InterceptHelper.computeIntercept(
                                mob.getPos(),
                                target.getPos(),
                                playerVel,
                                0.35 // charge speed, blocks per tick
                        );
                        timer = 100; // 5 seconds charge
                        state = State.CHARGING;
                    }
                    break;

                case CHARGING:
                    if (interceptPoint != null) {
                        Vec3d dir = interceptPoint.subtract(mob.getPos()).normalize();
                        mob.setVelocity(dir.multiply(0.35)); // constant velocity
                        mob.velocityModified = true;

                        // Knockback logic: anyone collided gets yeeted
                        mob.getWorld().getOtherEntities(mob, mob.getBoundingBox().expand(0.5),
                                e -> e instanceof LivingEntity && e != mob).forEach(e -> {
                            if (e instanceof LivingEntity living) {
                                // Knockback strength
                                float strength = 3.0f;

                                // Direction away from the Restless
                                double dx = e.getX() - mob.getX();
                                double dz = e.getZ() - mob.getZ();
                                double dist = Math.sqrt(dx * dx + dz * dz);
                                if (dist != 0) {
                                    dx /= dist;
                                    dz /= dist;

                                    // Apply knockback directly
                                    living.addVelocity(dx * strength, 0.4, dz * strength);
                                    living.velocityModified = true;
                                }
                            }

                        });
                    }

                    timer--;
                    if (timer <= 0) {
                        state = State.DONE;
                    }
                    break;

                case DONE:
                    mob.setAwakened(false);
                    state = State.DONE;
                    break;
            }
        }

        @Override
        public boolean shouldContinue() {
            return state != State.DONE;
        }
    }


}