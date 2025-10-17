package net.nullcoil.soulscorch.entity.client.soulcat;

import net.minecraft.client.render.entity.CatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class SoulborneCatRenderer extends CatEntityRenderer {
    private static final Identifier TEXTURE = Identifier.of(Soulscorch.MOD_ID, "textures/entity/midnight.png");

    public SoulborneCatRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(CatEntityRenderState state) { return TEXTURE; }
}
