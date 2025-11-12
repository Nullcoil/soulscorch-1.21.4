package net.nullcoil.soulscorch.nurvis.blockentity.glarynx;

import com.mojang.logging.LogUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.GameEventListener;
import net.nullcoil.soulscorch.block.ModBlockEntities;
import net.nullcoil.soulscorch.block.custom.GlarynxBlock;
import net.nullcoil.soulscorch.enums.GlarynxState;
import net.nullcoil.soulscorch.nurvis.blockentity.NurvisPacketHolderBlockEntity;
import net.nullcoil.soulscorch.util.ModTags;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class GlarynxBlockEntity extends NurvisPacketHolderBlockEntity implements GameEventListener.Holder<Vibrations.VibrationListener>, Vibrations {

    private static final Logger LOGGER = LogUtils.getLogger();
    private Vibrations.ListenerData listenerData;
    private final Vibrations.VibrationListener listener;
    private final Vibrations.Callback callback;
    private int lastVibrationFrequency;
    private GlarynxState currentState = GlarynxState.SLEEPY;
    private int watchfulTicks = 0;

    private GlarynxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.callback = this.createCallback();
        this.listenerData = new Vibrations.ListenerData();
        this.listener = new Vibrations.VibrationListener(this);
    }

    public GlarynxBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.GLARYNX, pos, state);
        if(world != null && !world.isClient()) {
            world.setBlockState(pos, state.with(GlarynxBlock.STATE, GlarynxState.SLEEPY), Block.NOTIFY_ALL);
        }
    }

    private void setState(GlarynxState newState) {
        if (this.currentState != newState) {
            Direction facing = this.getCachedState().get(GlarynxBlock.FACING);
            Vec3d facingVec = Vec3d.of(facing.getVector()).normalize();
            Vec3d eyePos = Vec3d.ofCenter(pos);
            LOGGER.info("[Glarynx placed] at {} facing {} → Eye position: {}", pos, facing, eyePos); // debug

            GlarynxState holdState = currentState;
            this.currentState = newState;
            LOGGER.info("Glarynx at {} changed state to {}", this.pos, newState); // debug

            if(this.world != null && !this.world.isClient()) {
                BlockState currentState = this.getCachedState();
                BlockState newBlockState = currentState.with(GlarynxBlock.STATE, newState);
                this.world.setBlockState(this.pos, newBlockState, Block.NOTIFY_ALL);
            }

            switch(newState) {
                case WATCHFUL -> {
                    assert this.getWorld() != null;
                    if(holdState==GlarynxState.SLEEPY) {
                        this.getWorld().playSound(null,
                                this.getPos(),
                                SoundEvents.BLOCK_SCULK_SENSOR_CLICKING,
                                SoundCategory.BLOCKS);
                    }
                }
            }
            this.markDirty();
        }
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if(world instanceof ServerWorld serverWorld) {
            Vibrations.VibrationListener listener = this.getEventListener();
            if(listener != null) {}
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, GlarynxBlockEntity entity) {
        if (world.isClient) return;

        Vibrations.Ticker.tick(world, entity.getVibrationListenerData(), entity.getVibrationCallback());

        if (entity.currentState == GlarynxState.WATCHFUL) {
            entity.watchfulTicks++;
            if (entity.watchfulTicks > 200) { // ~10 seconds
                entity.setState(GlarynxState.SLEEPY);
                entity.watchfulTicks = 0;
            }

            final double detectionRange = 8;
            Direction facing = state.get(GlarynxBlock.FACING);
            Vec3d facingVec = Vec3d.of(facing.getVector()).normalize();
            Vec3d eyePos = Vec3d.ofCenter(pos);

            List<PlayerEntity> nearbyPlayers = world.getEntitiesByClass(PlayerEntity.class,
                    new Box(pos).expand(detectionRange),
                    player -> !player.isSpectator() && !player.isCreative() && player.isAlive() && !player.hasStatusEffect(StatusEffects.INVISIBILITY));

            for(PlayerEntity player : nearbyPlayers) {
                if(isPlayerInVisionCone(player, eyePos, facingVec, detectionRange)) {
                    entity.setState(GlarynxState.SEEING);
                    entity.watchfulTicks = 0;
                    break;
                }
            }
        }

        if(entity.currentState == GlarynxState.SEEING) {
            boolean playerSeen = false;
            final double visionRange = 8;
            Direction facing = state.get(GlarynxBlock.FACING);
            Vec3d facingVec = Vec3d.of(facing.getVector()).normalize();
            Vec3d eyePos = Vec3d.ofCenter(pos);

            for(PlayerEntity player : world.getEntitiesByClass(PlayerEntity.class,
                    new Box(pos).expand(visionRange),
                    p -> !p.isSpectator() && !p.isCreative() &&
                            p.isAlive() &&
                            !p.hasStatusEffect(StatusEffects.INVISIBILITY))) {
                if(isPlayerInVisionCone(player, eyePos, facingVec, visionRange)) {
                    playerSeen = true;
                    break;
                }
            }

            if(!playerSeen) entity.setState(GlarynxState.WATCHFUL);
        }
    }

    private static boolean isPlayerInVisionCone(PlayerEntity player, Vec3d eyePos, Vec3d facingVec, double range) {
        Box playerBox = player.getBoundingBox();

        // Check multiple points of the player's bounding box
        Vec3d[] checkPoints = {
                new Vec3d(playerBox.minX, playerBox.minY, playerBox.minZ), // bottom NW
                new Vec3d(playerBox.maxX, playerBox.minY, playerBox.minZ), // bottom NE
                new Vec3d(playerBox.minX, playerBox.minY, playerBox.maxZ), // bottom SW
                new Vec3d(playerBox.maxX, playerBox.minY, playerBox.maxZ), // bottom SE
                new Vec3d(playerBox.minX, playerBox.maxY, playerBox.minZ), // top NW
                new Vec3d(playerBox.maxX, playerBox.maxY, playerBox.minZ), // top NE
                new Vec3d(playerBox.minX, playerBox.maxY, playerBox.maxZ), // top SW
                new Vec3d(playerBox.maxX, playerBox.maxY, playerBox.maxZ), // top SE
                player.getPos(), // center of player
                new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ()) // eye position
        };

        for (Vec3d point : checkPoints) {
            Vec3d toPoint = point.subtract(eyePos);
            double distance = toPoint.length();

            // Check if point is within range
            if (distance <= range) {
                toPoint = toPoint.normalize();
                double dot = facingVec.dotProduct(toPoint);

                // 30-degree cone (cos(30°) ≈ 0.866)
                if (dot > Math.cos(Math.toRadians(55))) {
                    return true;
                }
            }
        }

        return false;
    }

    public Vibrations.Callback createCallback() {
        return new GlarynxBlockEntity.VibrationCallback(this.getPos());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.lastVibrationFrequency = nbt.getInt("last_vibration_frequency");

        if(nbt.contains("watchful_timer")) {
            this.watchfulTicks = nbt.getInt("watchful_timer");
        }

        // Fix: Check if the state exists and is not empty
        String stateName = nbt.getString("glarynx_state");
        if (!stateName.isEmpty()) {
            try {
                this.currentState = GlarynxState.valueOf(stateName.toUpperCase());
            } catch (IllegalArgumentException e) {
                this.currentState = GlarynxState.SLEEPY;
                LOGGER.warn("Invalid glarynx state '{}' found in NBT, defaulting to SLEEPY", stateName);
            }
        } else {
            // If no state is saved, use the block state or default
            if (this.world != null && !this.world.isClient()) {
                BlockState blockState = this.getCachedState();
                if (blockState.contains(GlarynxBlock.STATE)) {
                    this.currentState = blockState.get(GlarynxBlock.STATE);
                } else {
                    this.currentState = GlarynxState.SLEEPY;
                }
            } else {
                this.currentState = GlarynxState.SLEEPY;
            }
        }

        RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
        if (nbt.contains("listener", 10)) {
            ListenerData.CODEC.parse(registryOps, nbt.getCompound("listener"))
                    .resultOrPartial((string) -> LOGGER.error("Failed to parse vibration listener for Glarynx: '{}'", string))
                    .ifPresent((listener) -> this.listenerData = listener);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("last_vibration_frequency", this.lastVibrationFrequency);

        // Only write the state if it's not null
        if (this.currentState != null) {
            nbt.putString("glarynx_state", this.currentState.name().toUpperCase());
        } else {
            nbt.putString("glarynx_state", GlarynxState.SLEEPY.name().toUpperCase());
        }

        nbt.putInt("watchful_timer", this.watchfulTicks / 20);

        RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
        ListenerData.CODEC.encodeStart(registryOps, this.listenerData)
                .resultOrPartial((string) -> LOGGER.error("Failed to encode vibration listener for Glarynx: '{}'", string))
                .ifPresent((listenerNbt) -> nbt.put("listener", listenerNbt));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);

        nbt.putInt("watchful_timer", this.watchfulTicks / 20);
        nbt.putString("current_state", this.currentState.name());
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public Vibrations.ListenerData getVibrationListenerData() {
        return this.listenerData;
    }

    public Vibrations.Callback getVibrationCallback() {
        return this.callback;
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    public void setLastVibrationFrequency(int lastVibrationFrequency) {
        this.lastVibrationFrequency = lastVibrationFrequency;
    }

    public Vibrations.VibrationListener getEventListener() {
        return this.listener;
    }

    protected class VibrationCallback implements Vibrations.Callback {
        public static final int RANGE = 8;
        protected final BlockPos pos;
        private final PositionSource positionSource;

        public VibrationCallback(final BlockPos pos) {
            this.pos = pos;
            this.positionSource = new BlockPositionSource(pos);
        }

        @Override
        public int getRange() {
            return RANGE;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean triggersAvoidCriterion() {
            return true;
        }

        @Override
        public boolean accepts(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event, @Nullable GameEvent.Emitter emitter) {

            if (emitter != null &&
                    emitter.sourceEntity() != null &&
                    !(emitter.sourceEntity().getType().isIn(ModTags.Entities.SOULSCORCH_ENTITIES))) {
                return true;
            }

            return false;
        }

        @Override
        public void accept(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event,
                           @Nullable Entity sourceEntity, @Nullable Entity entity, float distance) {
            GlarynxBlockEntity.this.setLastVibrationFrequency(Vibrations.getFrequency(event));

            // Move from SLEEPY to WATCHFUL when any vibration is detected
            if (GlarynxBlockEntity.this.currentState == GlarynxState.SLEEPY) {
                GlarynxBlockEntity.this.setState(GlarynxState.WATCHFUL);
            }

            // Reset the watchful timer
            GlarynxBlockEntity.this.watchfulTicks = 0;
        }

        @Override
        public void onListen() {
            GlarynxBlockEntity.this.markDirty();
        }

        @Override
        public boolean requiresTickingChunksAround() {
            return true;
        }
    }
}
