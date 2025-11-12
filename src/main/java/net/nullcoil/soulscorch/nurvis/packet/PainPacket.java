package net.nullcoil.soulscorch.nurvis.packet;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

public class PainPacket extends NurvisPacketParent {
    public PainPacket(ServerWorld world) {
        super(world, NurvisPacketType.PAIN);
    }

    @Override
    public void onArrive(ServerWorld world, BlockPos destination) {

    }
}
