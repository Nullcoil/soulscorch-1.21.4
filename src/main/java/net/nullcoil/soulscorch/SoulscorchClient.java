package net.nullcoil.soulscorch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.nullcoil.soulscorch.block.ModBlocks;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.client.blazt.BlaztModel;
import net.nullcoil.soulscorch.entity.client.blazt.BlaztRenderer;
import net.nullcoil.soulscorch.entity.client.restless.RestlessModel;
import net.nullcoil.soulscorch.entity.client.restless.RestlessRenderer;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessModel;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessRenderer;

public class SoulscorchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("SoulscorchClient initializing..."); // Debug

        // Register Soul Charge projectile
        EntityRendererRegistry.register(ModEntities.SOUL_CHARGE, FlyingItemEntityRenderer::new);

        // Register Blazt Entity - Model layer first, then renderer
        EntityModelLayerRegistry.registerModelLayer(BlaztModel.BLAZT, BlaztModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.BLAZT, BlaztRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(SoullessModel.SOULLESS, SoullessModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SOULLESS, SoullessRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(RestlessModel.RESTLESS, RestlessModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.RESTLESS, RestlessRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_BREWING_STAND, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_SLAG_BLOCK, RenderLayer.getCutout());

        System.out.println("SoulscorchClient initialization complete."); // Debug
    }
}