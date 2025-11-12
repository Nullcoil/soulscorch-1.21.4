package net.nullcoil.soulscorch.nurvis.packet.packets;

import net.minecraft.server.world.ServerWorld;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class AlertPacket extends NurvisPacketParent {
    public AlertPacket(ServerWorld world) { super(world, NurvisPacketType.ALERT); }

    @Override
    public boolean isNull() {
        return false;
    }
}
