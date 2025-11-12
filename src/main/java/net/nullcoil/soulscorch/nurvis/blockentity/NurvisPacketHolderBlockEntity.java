package net.nullcoil.soulscorch.nurvis.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NurvisPacketHolderBlockEntity extends BlockEntity {
    protected boolean hasPackets;
    private final List<@Nullable NurvisPacketParent> packets;

    protected NurvisPacketHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.hasPackets = false;
        // Initialize with 3 null slots
        this.packets = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            this.packets.add(null);
        }
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if(world instanceof ServerWorld sw) {
            // Initialize any null slots with NULL packets
            for (int i = 0; i < packets.size(); i++) {
                if(packets.get(i) == null) {
                    packets.set(i, NurvisPacketType.NULL.create(sw).build());
                }
            }
        }
    }

    public NurvisPacketParent getPacket(int slot) {
        return slot >= 0 && slot < packets.size() ? this.packets.get(slot) : null;
    }

    public void setPacket(int slot, NurvisPacketParent pkt) {
        this.packets.set(slot, pkt);
        this.markDirty();
    }

    public boolean insertPacket(NurvisPacketParent pkt) {
        for(int i = 0; i < packets.size(); i++) {
            NurvisPacketParent existing = packets.get(i);
            // Check for null OR isNull() to handle uninitialized slots
            if(existing == null || existing.isNull()) {
                packets.set(i, pkt);
                this.markDirty();
                return true;
            }
        }
        return false; // no room
    }

    public boolean createPacket(NurvisPacketType type) {
        return insertPacket(type.create(this.world).build());
    }

    public boolean pushPacket(ServerWorld world, int slot) {
        if(slot < 0 || slot >= packets.size()) return false;

        NurvisPacketParent packet = packets.get(slot);
        if(packet == null || packet.isNull()) return false;
        Direction[] order = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        for(Direction dir : order) {
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
        }

        this.killPacket(slot);
        return false; // none accepted
    }

    public boolean pushPacket(ServerWorld world) {
        Direction[] order = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        NurvisPacketParent packet = null;
        int slot = -1;

        // Find first non-null, non-NULL packet
        for(int i = 0; i < packets.size(); i++) {
            NurvisPacketParent p = packets.get(i);
            if(p != null && !p.isNull()) {
                packet = p;
                slot = i;
                break;
            }
        }

        if (packet == null || slot == -1) return false; // packets is empty

        for(Direction dir : order) {
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
        }

        this.killPacket(slot);
        return false;
    }

    public void killPacket(int slot) {
        if(slot >= 0 && slot < packets.size()) {
            // Handle case where world might not be set yet
            if(world instanceof ServerWorld sw) {
                packets.set(slot, NurvisPacketType.NULL.create(sw).build());
            } else {
                packets.set(slot, null);
            }
            this.markDirty();
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.hasPackets = hasPackets();
    }

    public boolean hasPackets() {
        for (NurvisPacketParent packet : packets) {
            if (packet != null && !packet.isNull()) return true;
        }
        return false;
    }
}