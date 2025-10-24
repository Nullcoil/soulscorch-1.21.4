package net.nullcoil.soulscorch.entity.client.hytodom;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.entity.custom.HytodomEntity;

public class HytodomRenderer extends MobEntityRenderer<HytodomEntity, HytodomRenderState, HytodomModel> {
    private static final Identifier TEXTURE =
            Identifier.of("soulscorch", "textures/entity/hytodom.png");

    public HytodomRenderer(EntityRendererFactory.Context context) {
        super(context, new HytodomModel(context.getPart(HytodomModel.HYTODOM)), 0F);
    }

    @Override
    public Identifier getTexture(HytodomRenderState state) {
        return TEXTURE;
    }

    @Override
    public HytodomRenderState createRenderState() {
        return new HytodomRenderState();
    }

    @Override
    public void updateRenderState(HytodomEntity entity, HytodomRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.IDLE.copyFrom(entity.IDLE);
    }
}
