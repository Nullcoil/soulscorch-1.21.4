package net.nullcoil.soulscorch.enums;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.nurvis.packet.packets.*;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketBuilder;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;

import java.util.function.Function;

public enum NurvisPacketType {
    ALERT(AlertPacket::new),    // Goes from Organ to Brain
    DECOY(DecoyPacket::new),    // Goes to Brain, Brain sends Pain to a random Glarynx
    MEMORY(MemoryPacket::new),  // Sent to Brain to tell it of organ's existence
    PAIN(PainPacket::new),      // Sent from Brain to Glarynx. Only type to go backward, and most complex,
                                // carrying instructions on what to do with the packet, according to its
                                // container
    NULL(NullPacket::new);

    private final Function<ServerWorld, NurvisPacketParent> factory;

    NurvisPacketType(Function<ServerWorld, NurvisPacketParent> factory) {
        this.factory = factory;
    }

    public NurvisPacketBuilder create(ServerWorld world) {
        return new NurvisPacketBuilder(factory.apply(world));
    }
    public NurvisPacketBuilder create(World world) {
        return create((ServerWorld) world);
    }
}