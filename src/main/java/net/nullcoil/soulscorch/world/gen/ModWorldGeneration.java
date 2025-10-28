package net.nullcoil.soulscorch.world.gen;

import net.nullcoil.soulscorch.Soulscorch;

public class ModWorldGeneration {
    public static void register() {
        Soulscorch.LOGGER.info("Registering World Gen for " + Soulscorch.MOD_ID);
        ModEntitySpawns.addSpawns();
    }
}