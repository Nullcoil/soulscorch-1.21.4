package net.nullcoil.soulscorch.nurvis.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.nullcoil.soulscorch.block.ModBlockEntities;
import net.nullcoil.soulscorch.block.custom.NurvisConduitBlock;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class NurvisConduitBlockEntity extends NurvisPacketHolderBlockEntity {
    private NurvisConduitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NurvisConduitBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.NURVIS_CONDUIT, pos, state);
    }

    @Override
    public boolean pushPacket(ServerWorld world) {
        Direction dir = getCachedState().get(NurvisConduitBlock.FACING);

        NurvisPacketParent packet = null;
        int slot = -1;

        // Find first non-null, non-NULL packet
        for(int i = 0; i < 3; i++) {
            NurvisPacketParent p = getPacket(i);
            if(p != null && !p.isNull()) {
                packet = p;
                slot = i;
                break;
            }
        }

        if (packet == null || slot == -1) return false; // inventory empty

        BlockPos targetPos = pos.offset(dir);
        BlockEntity be = world.getBlockEntity(targetPos);

        if(be instanceof NurvisPacketHolderBlockEntity target) {
            boolean success = target.insertPacket(packet);
            if(success) {
                packet.onArrive();
                this.killPacket(slot);
                return true;
            }
        }

        this.killPacket(slot);
        return false;
    }

    @Override
    public boolean pushPacket(ServerWorld world, int slot) {
        if(slot < 0 || slot >= 3) return false;

        NurvisPacketParent packet = getPacket(slot);
        if(packet == null || packet.isNull()) return false;
        Direction dir = getCachedState().get(NurvisConduitBlock.FACING);
        BlockPos targetPos = pos.offset(dir);
        BlockEntity be = world.getBlockEntity(targetPos);

        if(be instanceof NurvisPacketHolderBlockEntity target) {
            boolean success = target.insertPacket(packet);
            if(success) {
                packet.onArrive();
                this.killPacket(slot);
                return true;
            }
        }

        this.killPacket(slot);
        return false; // none accepted
    }
}
