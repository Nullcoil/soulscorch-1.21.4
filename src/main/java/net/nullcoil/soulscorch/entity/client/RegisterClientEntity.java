package net.nullcoil.soulscorch.entity.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.SoulscorchClient;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.client.blazt.BlaztModel;
import net.nullcoil.soulscorch.entity.client.blazt.BlaztRenderer;
import net.nullcoil.soulscorch.entity.client.jellyfish.JellyfishModel;
import net.nullcoil.soulscorch.entity.client.jellyfish.JellyfishRenderer;
import net.nullcoil.soulscorch.entity.client.restless.RestlessModel;
import net.nullcoil.soulscorch.entity.client.restless.RestlessRenderer;
import net.nullcoil.soulscorch.entity.client.soulcat.SoulborneCatRenderer;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessModel;
import net.nullcoil.soulscorch.entity.client.soulless.SoullessRenderer;

public class RegisterClientEntity {
    public static void register() {
        // Register Soul Charge projectile
        EntityRendererRegistry.register(ModEntities.SOUL_CHARGE, FlyingItemEntityRenderer::new);

        // Register Blazt Entity - Model layer first, then renderer
        EntityModelLayerRegistry.registerModelLayer(BlaztModel.BLAZT, BlaztModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.BLAZT, BlaztRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(SoullessModel.SOULLESS, SoullessModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SOULLESS, context ->
                new SoullessRenderer(context, SoullessModel.SOULLESS,
                        EntityModelLayers.PIGLIN_INNER_ARMOR,
                        EntityModelLayers.PIGLIN_OUTER_ARMOR));

        EntityModelLayerRegistry.registerModelLayer(RestlessModel.RESTLESS, RestlessModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.RESTLESS, RestlessRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "soulborne_cat"), "main"), SoulscorchClient::getCatTexturedModelData);
        EntityRendererRegistry.register(ModEntities.SOULBORNE_CAT, SoulborneCatRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "hytodom"), "main"), JellyfishModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HYTODOM, JellyfishRenderer::new);
    }
}
