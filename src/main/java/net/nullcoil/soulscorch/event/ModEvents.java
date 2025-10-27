package net.nullcoil.soulscorch.event;

import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.event.handlers.DamageEventHandler;
import net.nullcoil.soulscorch.event.handlers.SleepHealthResetHandler;
import net.nullcoil.soulscorch.event.handlers.SoulbreakEventHandler;

public class ModEvents {
    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Events for " + Soulscorch.MOD_ID);

        SoulbreakEventHandler.register();
        DamageEventHandler.register();
        SleepHealthResetHandler.register();
        SoulCampfireDetector.register();
    }
}
