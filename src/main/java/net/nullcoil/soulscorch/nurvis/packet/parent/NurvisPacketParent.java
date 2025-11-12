package net.nullcoil.soulscorch.nurvis.packet.parent;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.PacketInstruction;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NurvisPacketParent {

    protected BlockPos holderPos;
    protected final long creation;
    protected boolean expired;
    protected NurvisPacketType type;
    private Function<NurvisPacketParent, PacketInstruction> onArriveHandler = this::defaultInstruction;

    protected NurvisPacketParent(ServerWorld world, NurvisPacketType type) {
        this.creation = world.getTime();
        this.expired = false;
        this.type = type;
    }

    public boolean isExpired(ServerWorld world, long maxAgeTicks) {
        return expired || (world.getTime() - creation) > maxAgeTicks;
    }

    public NurvisPacketType getType() { return type; };
    public void onArrive() {
        onArriveHandler.apply(this);
    }

    protected PacketInstruction defaultInstruction(NurvisPacketParent packet) {
        // default behavior
        return new PacketInstruction(() -> {}, 0);
    }

    public void setOnArriveHandler(Function<NurvisPacketParent, PacketInstruction> handler) {
        this.onArriveHandler = handler;
    }

    public abstract boolean isNull();
}