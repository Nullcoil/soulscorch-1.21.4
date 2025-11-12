package net.nullcoil.soulscorch.nurvis.packet.packets;

import net.minecraft.server.world.ServerWorld;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class MemoryPacket extends NurvisPacketParent {
    public MemoryPacket(ServerWorld world) { super(world, NurvisPacketType.MEMORY); }

    @Override
    public boolean isNull() {
        return false;
    }
}
