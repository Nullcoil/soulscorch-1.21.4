package net.nullcoil.soulscorch.entity.client.jellyfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.entity.custom.HytodomEntity;

public class JellyfishRenderer extends MobEntityRenderer<HytodomEntity, JellyfishRenderState, JellyfishModel> {
    private static final Identifier TEXTURE =
            Identifier.of("soulscorch", "textures/entity/hytodom.png");

    public JellyfishRenderer(EntityRendererFactory.Context context) {
        super(context, new JellyfishModel(context.getPart(JellyfishModel.HYTODOM)), 0F);
    }

    @Override
    public Identifier getTexture(JellyfishRenderState state) {
        return TEXTURE;
    }

    @Override
    public JellyfishRenderState createRenderState() {
        return new JellyfishRenderState();
    }

    @Override
    public void updateRenderState(HytodomEntity entity, JellyfishRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.IDLE.copyFrom(entity.IDLE);
    }
}