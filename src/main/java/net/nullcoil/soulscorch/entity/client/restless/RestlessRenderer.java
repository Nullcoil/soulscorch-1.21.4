package net.nullcoil.soulscorch.entity.client.restless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractHoglinEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.HoglinEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.HoglinEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.entity.custom.RestlessEntity;

@Environment(EnvType.CLIENT)
public class RestlessRenderer extends MobEntityRenderer<RestlessEntity, RestlessRenderState, RestlessModel> {
    private static final Identifier TEXTURE =
            Identifier.of("soulscorch", "textures/entity/restless/restless.png");
    private static final Identifier AWAKENED =
            Identifier.of("soulscorch", "textures/entity/restless/restless_awakened.png");

    public RestlessRenderer(EntityRendererFactory.Context context) {
        super(context, new RestlessModel(context.getPart(RestlessModel.RESTLESS)), 0.7f);
    }

    @Override
    public void updateRenderState(RestlessEntity entity, RestlessRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.awakened = entity.getAwakened();
    }

    @Override
    public Identifier getTexture(RestlessRenderState state) { return state.awakened ? AWAKENED : TEXTURE; }

    @Override
    public RestlessRenderState createRenderState() { return new RestlessRenderState(); }
}
