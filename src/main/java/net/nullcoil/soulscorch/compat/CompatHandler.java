package net.nullcoil.soulscorch.compat;

import net.fabricmc.loader.api.FabricLoader;

public class CompatHandler {
    public static void register() {
        if(isModLoaded("id")) {
            // stuff and things
        }
    }

    private static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
