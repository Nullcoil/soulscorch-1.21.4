package net.nullcoil.soulscorch.nurvis.packet.parent;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.nurvis.packet.PacketInstruction;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class NurvisPacketBuilder {
    private final NurvisPacketParent packet;

    public NurvisPacketBuilder(NurvisPacketParent packet) {
        this.packet = packet;
    }

    public NurvisPacketBuilder overrideOnArrive(Function<NurvisPacketParent, PacketInstruction> customHandler) {
        packet.setOnArriveHandler(customHandler);
        return this;
    }

    public NurvisPacketParent build() {
        return packet;
    }
}
