package net.nullcoil.soulscorch.nurvis.packet;

public class PacketInstruction {
    public final Runnable action;
    public final int priority;

    public PacketInstruction(Runnable action, int priority) {
        this.action = action;
        this.priority = priority;
    }
}
