package net.nullcoil.soulscorch.entity.client.blazt;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;

@Environment(EnvType.CLIENT)
public class BlaztRenderer extends MobEntityRenderer<BlaztEntity, BlaztRenderState, BlaztModel> {
    private static final Identifier TEXTURE =
            Identifier.of("soulscorch", "textures/entity/blazt/blazt.png");
    private static final Identifier SHOOTING_TEXTURE =
            Identifier.of("soulscorch", "textures/entity/blazt/blazt_shooting.png");

    public BlaztRenderer(EntityRendererFactory.Context context) {
        super(context, new BlaztModel(context.getPart(BlaztModel.BLAZT)), 1.5F);
    }

    @Override
    public BlaztRenderState createRenderState() {
        return new BlaztRenderState();
    }

    @Override
    public void updateRenderState(BlaztEntity entity, BlaztRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.shooting = entity.isShooting();

        // state.shooting = entity.isShooting();
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.shootAnimationState.copyFrom(entity.shootAnimationState);
    }

    @Override
    public Identifier getTexture(BlaztRenderState state) {
        return state.shooting ? SHOOTING_TEXTURE : TEXTURE;
    }
}
