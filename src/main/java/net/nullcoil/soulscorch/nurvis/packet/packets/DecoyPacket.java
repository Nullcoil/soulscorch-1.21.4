package net.nullcoil.soulscorch.nurvis.packet.packets;

import net.minecraft.server.world.ServerWorld;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class DecoyPacket extends NurvisPacketParent {
    public DecoyPacket(ServerWorld world) { super(world, NurvisPacketType.DECOY); }

    @Override
    public boolean isNull() {
        return false;
    }
}
