package net.nullcoil.soulscorch.block.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.GameEventListener;
import net.nullcoil.soulscorch.block.ModBlockEntities;
import net.nullcoil.soulscorch.block.custom.GlarynxBlock;
import net.nullcoil.soulscorch.block.entity.glarynx.GlarynxState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class GlarynxBlockEntity extends BlockEntity implements GameEventListener.Holder<Vibrations.VibrationListener>, Vibrations {

    private static final Logger LOGGER = LogUtils.getLogger();
    private Vibrations.ListenerData listenerData;
    private final Vibrations.VibrationListener listener;
    private final Vibrations.Callback callback;
    private int lastVibrationFrequency;
    private GlarynxState currentState = GlarynxState.SLEEPY;
    private int watchfulTicks = 0;

    protected GlarynxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
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
            this.currentState = newState;
            LOGGER.info("Glarynx at {} changed state to {}", this.pos, newState);

            if(this.world != null && !this.world.isClient()) {
                BlockState newBlockState = this.getCachedState().with(GlarynxBlock.STATE, newState);
                this.world.setBlockState(this.pos, newBlockState, Block.NOTIFY_ALL);
            }

            switch(newState) {
                case WATCHFUL -> {
                    assert this.getWorld() != null;
                    this.getWorld().playSound(null,
                                              this.getPos(),
                                              SoundEvents.BLOCK_SCULK_SENSOR_CLICKING,
                                              SoundCategory.BLOCKS);
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
        }
    }

    public Vibrations.Callback createCallback() {
        return new GlarynxBlockEntity.VibrationCallback(this.getPos());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.lastVibrationFrequency = nbt.getInt("last_vibration_frequency");
        this.currentState = GlarynxState.valueOf(nbt.getString("glarynx_state"));

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
        nbt.putString("glarynx_state", this.currentState.name());
        RegistryOps<NbtElement> registryOps = registries.getOps(NbtOps.INSTANCE);
        ListenerData.CODEC.encodeStart(registryOps, this.listenerData)
                .resultOrPartial((string) -> LOGGER.error("Failed to encode vibration listener for Glarynx: '{}'", string))
                .ifPresent((listenerNbt) -> nbt.put("listener", listenerNbt));
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
            // Only listen when idle
            return GlarynxBlockEntity.this.currentState == GlarynxState.SLEEPY;
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
