package net.nullcoil.soulscorch.screen;

import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SoulBrewingStandScreenHandler> SOUL_BREWING_STAND_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of("soulscorch", "soul_brewing_stand_screen_handler"),
                    new ScreenHandlerType<>(SoulBrewingStandScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    public static void register() {
        Soulscorch.LOGGER.info("Registering Mod Screen Handlers for " + Soulscorch.MOD_ID);
    }
}
