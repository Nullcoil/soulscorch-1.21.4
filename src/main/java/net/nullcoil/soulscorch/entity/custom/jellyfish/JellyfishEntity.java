package net.nullcoil.soulscorch.entity.custom.jellyfish;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class JellyfishEntity extends FlyingEntity {
    public final AnimationState IDLE = new AnimationState();
    private static final TrackedData<BlockPos> TARGET_POS = DataTracker.registerData(JellyfishEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TARGET_POS, BlockPos.ORIGIN);
    }

    public void updateClientTarget(double x, double y, double z) {
        if(!this.getWorld().isClient()) {
            this.dataTracker.set(TARGET_POS, BlockPos.ofFloored(x,y,z));
        }
    }

    public BlockPos getClientTarget() {
        return this.dataTracker.get(TARGET_POS);
    }

    public double getClientTargetX() {
        return this.dataTracker.get(TARGET_POS).getX();
    }

    public double getClientTargetY() {
        return this.dataTracker.get(TARGET_POS).getY();
    }

    public double getClientTargetZ() {
        return this.dataTracker.get(TARGET_POS).getZ();
    }

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
                .add(EntityAttributes.MOVEMENT_SPEED, 0.025)
                .add(EntityAttributes.ATTACK_DAMAGE, 2);
    }

    @Override
    public void initGoals() {
        this.goalSelector.add(0, new JellyfishEntity.FlyRandomlyGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getWorld().isClient) {
            this.IDLE.startIfNotRunning(this.age);
        }

        if(!this.getWorld().isClient) {
            this.getWorld().getOtherEntities(this, this.getBoundingBox(),
                    e -> e instanceof LivingEntity && !e.getType().isIn(ModTags.Entities.SOULSCORCH_ENTITIES)).forEach(e -> {
                LivingEntity living = (LivingEntity) e;
                double dx = living.getX() - this.getX();
                double dz = living.getZ() - this.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist != 0) {
                    dx /= dist;
                    dz /= dist;

                    living.damage(
                            (ServerWorld) this.getWorld(),
                            this.getDamageSources().mobAttack(this),
                            (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE)
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
            });
        }
    }

    public static class JellyfishMoveControl extends MoveControl {
        private static final double REACHED_DESTINATION_DISTANCE_SQUARED = 1.0;
        private static final double HORIZONTAL_DAMPING = 0.98;
        private static final double VERTICAL_DAMPING = 0.95;
        private boolean hasActiveTarget = false;
        JellyfishEntity jelly;

        public JellyfishMoveControl(JellyfishEntity jelly) {
            super(jelly);
            this.jelly = jelly;
        }

        @Override
        public void moveTo(double x, double y, double z, double speed) {
            super.moveTo(x, y, z, speed);
            if(this.jelly != null) {
                this.jelly.updateClientTarget(x,y,z);
            }
        }

        @Override
        public void tick() {
            if (this.isMoving()) {
                double dx = this.targetX - this.entity.getX();
                double dy = this.targetY - this.entity.getY();
                double dz = this.targetZ - this.entity.getZ();

                double distSq = dx * dx + dy * dy + dz * dz;

                if(this.jelly != null && this.jelly.age%10==0) {
                    this.jelly.updateClientTarget(this.targetX, this.targetY, this.targetZ);
                }

                if (distSq < REACHED_DESTINATION_DISTANCE_SQUARED) {
                    this.entity.setVelocity(this.entity.getVelocity().multiply(0.5));
                    this.state = MoveControl.State.WAIT;
                    this.hasActiveTarget = false;
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

        public boolean hasActiveTarget() {
            return this.hasActiveTarget;
        }
    }

    @Override
    public boolean isFireImmune() { return true; }

    static class FlyRandomlyGoal extends Goal {
        private final JellyfishEntity jelly;

        public FlyRandomlyGoal(JellyfishEntity jelly) {
            this.jelly = jelly;
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

            for (int i = 0; i < 16; i++) {
                // Random movement in all directions
                double targetX = this.jelly.getX() + (random.nextDouble() * 2.0 - 1.0) * 16.0;
                double targetY = this.jelly.getY() + (random.nextDouble() * 2.0 - 1.0) * 16.0;
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
        }

        private boolean isAreaClear(World world, BlockPos pos) {
            return world.isAir(pos)
                    && world.isAir(pos.up())
                    && world.isAir(pos.down())
                    && world.isAir(pos.north())
                    && world.isAir(pos.south())
                    && world.isAir(pos.east())
                    && world.isAir(pos.west());
        }
    }

    @Override
    public boolean canSpawn(WorldView world) {
        // Allow both ground AND air spawning
        return canSpawnOnGround(world) || canSpawnInAir(world);
    }

    private boolean canSpawnOnGround(WorldView world) {
        BlockPos pos = this.getBlockPos();
        BlockPos groundPos = pos.down();

        // Standard ground spawn checks
        return world.getBlockState(groundPos).isSolid()
                && world.getBlockState(pos).isAir()
                && world.getBaseLightLevel(pos, 0) <= 11
                && !isTooDense(world, pos)
                && (world.getBlockState(groundPos) != Blocks.BEDROCK.getDefaultState());
    }

    private boolean canSpawnInAir(WorldView world) {
        BlockPos pos = this.getBlockPos();

        // Air spawn checks - must have adequate air space
        if (!world.getBlockState(pos).isAir()) {
            return false;
        }

        // Check we have at least 2 blocks of air in all directions
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = pos.add(x, y, z);
                    if (!world.getBlockState(checkPos).isAir()) {
                        return false;
                    }
                }
            }
        }

        return world.getBaseLightLevel(pos, 0) <= 11
                && !isTooDense(world, pos)
                && this.getBlockPos().getY() < 125;
    }

    private boolean isTooDense(WorldView world, BlockPos pos) {
        // Density check for both ground and air spawns
        List<JellyfishEntity> nearbyJellyfish = this.getWorld().getEntitiesByClass(
                JellyfishEntity.class,
                this.getBoundingBox().expand(16),
                entity -> true
        );

        // Allow up to 4 jellyfish in 16 block radius
        return nearbyJellyfish.size() > 3;
    }

    // Static method for spawn restriction system
    public static boolean canSpawn(EntityType<? extends JellyfishEntity> type, ServerWorldAccess world,
                                   SpawnReason spawnReason, BlockPos pos, Random random) {
        if (spawnReason == SpawnReason.NATURAL) {
            // Create dummy entity to check spawn conditions
            JellyfishEntity dummy = new JellyfishEntity(type, world.toServerWorld());
            dummy.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            return dummy.canSpawn(world);
        }
        return true;
    }
}