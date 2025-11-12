package net.nullcoil.soulscorch.nurvis.packet.packets;

import net.minecraft.server.world.ServerWorld;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class NullPacket extends NurvisPacketParent {
    public NullPacket(ServerWorld world) { super(world, NurvisPacketType.NULL); }

    @Override
    public boolean isNull() {
        return true;
    }
}
