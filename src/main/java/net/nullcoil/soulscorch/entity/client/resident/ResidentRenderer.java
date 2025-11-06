package net.nullcoil.soulscorch.entity.client.resident;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.entity.custom.ResidentEntity;

public class ResidentRenderer extends MobEntityRenderer<ResidentEntity, ResidentRenderState, ResidentModel> {
    private static final Identifier TEXTURE =
            Identifier.of("soulscorch", "textures/entity/resident.png");

    public ResidentRenderer(EntityRendererFactory.Context context) {
        super(context, new ResidentModel(context.getPart(ResidentModel.RESIDENT)), 0F);
    }

    @Override
    public Identifier getTexture(ResidentRenderState state) {
        return TEXTURE;
    }

    @Override
    public ResidentRenderState createRenderState() {
        return new ResidentRenderState();
    }
}
