package net.nullcoil.soulscorch.entity.client.restless;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

public class AnimatedManeRenderLayer extends RenderLayer {

    private AnimatedManeRenderLayer(String name, VertexFormat vertexFormat,
                                    VertexFormat.DrawMode drawMode, int expectedBufferSize,
                                    boolean hasCrumbling, boolean translucent,
                                    Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent,
                startAction, endAction);
    }

    public static RenderLayer getAnimatedEmissive(Identifier texture) {
        return RenderLayer.of(
                "animated_mane_emissive",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.EYES_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .writeMaskState(RenderPhase.COLOR_MASK)
                        .build(false)
        );
    }
}