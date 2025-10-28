package net.nullcoil.soulscorch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.entity.client.RegisterClientEntity;
import net.nullcoil.soulscorch.gui.screen.SoulBrewingStandScreen;
import net.nullcoil.soulscorch.screen.ModScreenHandlers;

public class SoulscorchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Soulscorch.LOGGER.info("Soulscorch Clientside features initializing..."); // Debug

        RegisterClientEntity.register();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_BREWING_STAND, RenderLayer.getCutout());
        // BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_SLAG_BLOCK, RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.SOUL_BREWING_STAND_SCREEN_HANDLER, SoulBrewingStandScreen::new);
    }

    public static TexturedModelData getCatTexturedModelData() {
        ModelData modelData = CatEntityModel.getModelData(Dilation.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }
}