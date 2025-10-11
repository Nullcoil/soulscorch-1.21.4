package net.nullcoil.soulscorch.entity.client.restless;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RestlessManeFeatureRenderer extends FeatureRenderer<RestlessRenderState, RestlessModel> {
    private static final Identifier MANE_TEXTURE =
            Identifier.of("soulscorch", "textures/entity/restless/restless_mane.png");

    // Animation settings - adjust these as needed
    private final int frameCount;
    private final float ticksPerFrame;

    public RestlessManeFeatureRenderer(FeatureRendererContext<RestlessRenderState, RestlessModel> context) {
        this(context, 4, 4.0f);
    }

    public RestlessManeFeatureRenderer(FeatureRendererContext<RestlessRenderState, RestlessModel> context,
                                       int frameCount, float ticksPerFrame) {
        super(context);
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       RestlessRenderState state, float limbAngle, float limbDistance) {
        // Only render mane when awakened
        if (!state.awakened) {
            return;
        }

        // Calculate which frame to show based on age
        int currentFrame = (int)(state.age / ticksPerFrame) % frameCount;
        float vOffset = (float)currentFrame / (float)frameCount;
        float vScale = 1.0f / (float)frameCount;

        // Get the animated vertex consumer that transforms UVs
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(
                AnimatedManeRenderLayer.getAnimatedEmissive(MANE_TEXTURE)
        );

        // Wrap it to transform UV coordinates
        VertexConsumer animatedConsumer = new AnimatedUVVertexConsumer(baseConsumer, vOffset, vScale);

        // Get the model
        RestlessModel model = this.getContextModel();

        // Apply body transformation before rendering mane
        // Since mane is a child of body, we need body's transformation applied
        matrices.push();
        model.getBody().rotate(matrices);

        // Render mane with full brightness (emissive)
        for (var manePart : model.getMane(state)) {
            manePart.render(matrices, animatedConsumer, 15728880, OverlayTexture.DEFAULT_UV);
        }

        matrices.pop();
    }

    /**
     * VertexConsumer wrapper that transforms V coordinates for animation
     */
    private static class AnimatedUVVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float vOffset;
        private final float vScale;

        public AnimatedUVVertexConsumer(VertexConsumer delegate, float vOffset, float vScale) {
            this.delegate = delegate;
            this.vOffset = vOffset;
            this.vScale = vScale;
        }

        @Override
        public VertexConsumer vertex(float x, float y, float z) {
            return delegate.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return delegate.color(red, green, blue, alpha);
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            // Transform V coordinate: scale it down and offset to correct frame
            float transformedV = v * vScale + vOffset;
            return delegate.texture(u, transformedV);
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            return delegate.overlay(u, v);
        }

        @Override
        public VertexConsumer light(int u, int v) {
            return delegate.light(u, v);
        }

        @Override
        public VertexConsumer light(int uv) {
            return delegate.light(uv);
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return delegate.normal(x, y, z);
        }
    }
}