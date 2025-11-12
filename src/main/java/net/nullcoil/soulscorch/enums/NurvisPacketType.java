package net.nullcoil.soulscorch.enums;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.nurvis.packet.AlertPacket;
import net.nullcoil.soulscorch.nurvis.packet.PainPacket;
import net.nullcoil.soulscorch.nurvis.packet.MemoryPacket;
import net.nullcoil.soulscorch.nurvis.packet.parent.NurvisPacketParent;
import net.nullcoil.soulscorch.nurvis.packet.DecoyPacket;

import java.util.function.Function;

public enum NurvisPacketType {
    ALERT(AlertPacket::new),    // Goes from Organ to Brain
    DECOY(DecoyPacket::new),    // Goes to Brain, Brain sends Pain to a random Glarynx
    MEMORY(MemoryPacket::new),  // Sent to Brain to tell it of organ's existence
    PAIN(PainPacket::new);      // Sent from Brain to Glarynx

    private final Function<ServerWorld, NurvisPacketParent> factory;

    NurvisPacketType(Function<ServerWorld, NurvisPacketParent> factory) {
        this.factory = factory;
    }

    public NurvisPacketParent create(ServerWorld world) {
        return factory.apply(world);
    }
    public NurvisPacketParent create(World world) {
        return create((ServerWorld) world);
    }
}