package net.nullcoil.soulscorch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.event.*;
import net.nullcoil.soulscorch.item.ModItems;
import net.nullcoil.soulscorch.potion.ModPotions;
import net.nullcoil.soulscorch.screen.ModScreenHandlers;
import net.nullcoil.soulscorch.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Soulscorch implements ModInitializer {
    public static final String MOD_ID = "soulscorch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModEffects.register();
        ModEntities.register();
        ModEvents.register();

        ModItems.register();
        ModPotions.register();
        ModScreenHandlers.register();
        ModSounds.register();

        if(FabricLoader.getInstance().isModLoaded("stylishstiles")) {
            System.out.println("Mod detected."); // will be making adjustments to this
                                                 // to add compatibility with other mods
        }
    }
}