package net.nullcoil.soulscorch.event;

import net.nullcoil.soulscorch.Soulscorch;

public class ModEvents {
    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Events for " + Soulscorch.MOD_ID);

        SoulbreakEventHandler.register();
        DamageEventHandler.register();
        SleepHealthResetHandler.register();
        SoulCampfireDetector.register();
    }
}
