package net.nullcoil.soulscorch.entity.client.soulless;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;

public class SoullessRenderer extends MobEntityRenderer<SoullessEntity, SoullessRenderState, SoullessModel> {
    private static final Identifier TEXTURE =
            Identifier.of(Soulscorch.MOD_ID, "textures/entity/soulless");

    public SoullessRenderer(EntityRendererFactory.Context context) {
        super(context, new SoullessModel(context.getPart(SoullessModel.SOULLESS)),1.5f);
    }

    @Override
    public SoullessRenderState createRenderState() {
        return new SoullessRenderState();
    }

    public void updateRenderState(SoullessEntity entity, SoullessRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.currentActivity = entity.getActivity();

        // Copy all animation states from entity to render state
        state.passiveAnimationState.copyFrom(entity.passiveAnimationState);
        state.neutralAnimationState.copyFrom(entity.neutralAnimationState);
        state.hostileAnimationState.copyFrom(entity.hostileAnimationState);
    }

    @Override
    public Identifier getTexture(SoullessRenderState state) {
        return TEXTURE;
    }
}