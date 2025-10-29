package net.nullcoil.soulscorch.entity.client.jellyfish;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class JellyfishEmissiveFeatureRenderer extends FeatureRenderer<JellyfishRenderState, JellyfishModel> {
    private static final Identifier GLOWEY_BITS =
            Identifier.of(Soulscorch.MOD_ID, "textures/entity/hytodom_emissive.png");

    public JellyfishEmissiveFeatureRenderer(FeatureRendererContext<JellyfishRenderState, JellyfishModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, JellyfishRenderState state, float limbAngle, float limbDistance) {
        VertexConsumer emissiveConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEyes(GLOWEY_BITS)
        );

        this.getContextModel().render(
                matrices,
                emissiveConsumer,
                0xF000F0, // Full brightness
                OverlayTexture.DEFAULT_UV
        );
    }
}
