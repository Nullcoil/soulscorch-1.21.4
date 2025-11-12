package net.nullcoil.soulscorch.enums;

import net.minecraft.util.StringIdentifiable;

public enum GlarynxState implements StringIdentifiable {
    COOLDOWN,
    SLEEPY,     // idle listener
    WATCHFUL,   // heard something, scanning
    SEEING,     // player in FOV
    SCREAMING;  // triggered by redstone, or another signal

    @Override
    public String asString() {
        return name().toLowerCase();
    }
}
