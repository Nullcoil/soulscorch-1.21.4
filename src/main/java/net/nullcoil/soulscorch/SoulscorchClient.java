package net.nullcoil.soulscorch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.client.BlaztModel;
import net.nullcoil.soulscorch.entity.client.BlaztRenderer;

public class SoulscorchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.SOUL_CHARGE, FlyingItemEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(BlaztModel.BLAZT, BlaztModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.BLAZT, BlaztRenderer::new);

    }
}
