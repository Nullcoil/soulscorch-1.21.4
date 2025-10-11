package net.nullcoil.soulscorch.entity.client.restless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
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

        // Add the custom mane feature renderer with 4 frames at 4 ticks per frame
        // Adjust these numbers as needed when you create your texture
        this.addFeature(new RestlessManeFeatureRenderer(this, 4, 4.0f));
    }

    @Override
    public void updateRenderState(RestlessEntity entity, RestlessRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.awakened = entity.getAwakened();
        state.age = entity.age; // used for mane animation
    }

    @Override
    public Identifier getTexture(RestlessRenderState state) {
        return state.awakened ? AWAKENED : TEXTURE;
    }

    @Override
    public RestlessRenderState createRenderState() {
        return new RestlessRenderState();
    }
}