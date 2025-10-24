package net.nullcoil.soulscorch.entity.custom.jellyfish;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class JellyfishEntity extends FlyingEntity {
    public final AnimationState IDLE = new AnimationState();

    public JellyfishEntity(EntityType<? extends JellyfishEntity> type, World world) {
        super(type, world);
        this.moveControl = new JellyfishMoveControl(this);
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
                .add(EntityAttributes.MOVEMENT_SPEED, 0.025);
    }
    
    @Override
    public void initGoals() {
        this.goalSelector.add(0, new JellyfishEntity.FlyRandomlyGoal(this, 32, 110));
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getWorld().isClient) {
            this.IDLE.startIfNotRunning(this.age);
        }
    }

    static class JellyfishMoveControl extends MoveControl {
        private static final double REACHED_DESTINATION_DISTANCE_SQUARED = 1.0;
        private static final double HORIZONTAL_DAMPING = 0.98;
        private static final double VERTICAL_DAMPING = 0.95;

        public JellyfishMoveControl(JellyfishEntity jelly) {
            super(jelly);
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                double dx = this.targetX - this.entity.getX();
                double dy = this.targetY - this.entity.getY();
                double dz = this.targetZ - this.entity.getZ();

                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < REACHED_DESTINATION_DISTANCE_SQUARED) {
                    this.entity.setVelocity(this.entity.getVelocity().multiply(0.5));
                    this.state = MoveControl.State.WAIT;
                    return;
                }

                double dist = Math.sqrt(distSq);
                dx /= dist; dy /= dist; dz /= dist;

                double speed = this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED);

                this.entity.setVelocity(
                        this.entity.getVelocity().add(
                                dx * speed * 0.1,
                                dy * speed * 0.1,
                                dz * speed * 0.1
                        )
                );

                // Keep orientation constant — no facing adjustment
                this.entity.setYaw(this.entity.getYaw());
                this.entity.setPitch(this.entity.getPitch());
            } else {
                // idle drifting slowdown
                this.entity.setVelocity(
                        this.entity.getVelocity().multiply(HORIZONTAL_DAMPING, VERTICAL_DAMPING, HORIZONTAL_DAMPING)
                );
            }
        }
    }

    @Override
    public boolean isFireImmune() { return true; }

    static class FlyRandomlyGoal extends Goal {
        private final JellyfishEntity jelly;
        private final double minY;
        private final double maxY;

        public FlyRandomlyGoal(JellyfishEntity jelly, double minY, double maxY) {
            this.jelly = jelly;
            this.minY = minY;
            this.maxY = maxY;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            MoveControl moveControl = this.jelly.getMoveControl();
            if (!moveControl.isMoving()) return true;

            double dx = moveControl.getTargetX() - this.jelly.getX();
            double dy = moveControl.getTargetY() - this.jelly.getY();
            double dz = moveControl.getTargetZ() - this.jelly.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            return distSq < 1.0 || distSq > 3600.0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.jelly.getRandom();
            World world = this.jelly.getWorld();

            for (int i = 0; i < 16; i++) { // try up to 16 random positions
                double targetX = this.jelly.getX() + (random.nextDouble() * 2.0 - 1.0) * 16.0;
                double targetY = MathHelper.clamp(
                        this.jelly.getY() + (random.nextDouble() * 2.0 - 1.0) * 16.0,
                        minY, maxY
                );
                double targetZ = this.jelly.getZ() + (random.nextDouble() * 2.0 - 1.0) * 16.0;
                BlockPos targetPos = BlockPos.ofFloored(targetX, targetY, targetZ);

                // Must be surrounded by air
                if (!isAreaClear(world, targetPos)) continue;

                // Raycast from current position to target to ensure path is unobstructed
                Vec3d currentPos = this.jelly.getPos();
                Vec3d targetVec = new Vec3d(targetX, targetY, targetZ);
                HitResult hit = world.raycast(
                        new RaycastContext(
                                currentPos, targetVec,
                                RaycastContext.ShapeType.COLLIDER,
                                RaycastContext.FluidHandling.NONE,
                                this.jelly
                        )
                );

                if (hit.getType() == HitResult.Type.MISS) {
                    // Path is clear — move there
                    this.jelly.getMoveControl().moveTo(targetX, targetY, targetZ, 1.0);
                    return;
                }
            }
            // no valid air target found, jelly will just idle and drift this tick
        }

        private boolean isAreaClear(World world, BlockPos pos) {
            // Check that the candidate and surrounding blocks are all air
            return world.isAir(pos)
                    && world.isAir(pos.up())
                    && world.isAir(pos.down())
                    && world.isAir(pos.north())
                    && world.isAir(pos.south())
                    && world.isAir(pos.east())
                    && world.isAir(pos.west());
        }
    }
}
