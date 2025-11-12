package net.nullcoil.soulscorch.nurvis.packet.parent;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.enums.NurvisPacketType;

public abstract class NurvisPacketParent {

    protected BlockPos holderPos;
    protected final long creation;
    protected boolean expired;
    protected NurvisPacketType type;

    protected NurvisPacketParent(ServerWorld world, NurvisPacketType type) {
        this.creation = world.getTime();
        this.expired = false;
        this.type = type;
    }

    public boolean isExpired(ServerWorld world, long maxAgeTicks) {
        return expired || (world.getTime() - creation) > maxAgeTicks;
    }

    public NurvisPacketType getType() { return type; };
    public abstract void onArrive(ServerWorld world, BlockPos destination);
}

