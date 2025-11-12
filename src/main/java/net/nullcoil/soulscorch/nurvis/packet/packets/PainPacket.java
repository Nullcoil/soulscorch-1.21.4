package net.nullcoil.soulscorch.nurvis.packet.packets;

import net.minecraft.server.world.ServerWorld;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class PainPacket extends NurvisPacketParent {
    public PainPacket(ServerWorld world) {
        super(world, NurvisPacketType.PAIN);
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
