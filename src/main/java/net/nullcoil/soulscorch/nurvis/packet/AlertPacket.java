package net.nullcoil.soulscorch.nurvis.packet;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class AlertPacket extends NurvisPacketParent {
    public AlertPacket(ServerWorld world) { super(world, NurvisPacketType.ALERT); }

    @Override
    public void onArrive(ServerWorld world, BlockPos destination) {

    }
}
