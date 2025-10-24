package net.nullcoil.soulscorch.entity.client.jellyfish;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class JellyfishModel extends EntityModel<JellyfishRenderState> {
    public static final EntityModelLayer HYTODOM = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "hytodom"), "main");
    private final ModelPart bell;
    private final ModelPart fringe;

    public JellyfishModel(ModelPart root) {
        super(root);
        this.bell = root.getChild("bell");
        this.fringe = this.bell.getChild("fringe");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bell = modelPartData.addChild("bell", ModelPartBuilder.create().uv(40, 20)
                .cuboid(-5.0F, -36.0F, -5.0F, 10.0F, 4.0F, 10.0F,
                        new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = bell.addChild("tentacle_1", ModelPartBuilder.create().uv(20, 20)
                .cuboid(0.0F, -1.0F, -5.0F, 0.0F, 32.0F, 10.0F,
                        new Dilation(0.0F)), ModelTransform.of(0.0F, -32.0F, 0.0F,
                0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r2 = bell.addChild("tentacle_2", ModelPartBuilder.create().uv(0, 20)
                .cuboid(0.0F, -1.0F, -5.0F, 0.0F, 32.0F, 10.0F,
                        new Dilation(0.0F)), ModelTransform.of(0.0F, -32.0F, 0.0F,
                0.0F, -0.7854F, 0.0F));

        ModelPartData fringe = bell.addChild("fringe", ModelPartBuilder.create().uv(0, 0)
                .cuboid(-8.0F, 0.0F, -8.0F, 16.0F, 4.0F, 16.0F,
                        new Dilation(0.0F)), ModelTransform.pivot(0.0F, -34.0F, 0.0F));
        return TexturedModelData.of(modelData, 80, 62);
    }

    @Override
    public void setAngles(JellyfishRenderState state) {
        super.setAngles(state);

        this.getRootPart().traverse().forEach(ModelPart::resetTransform);
        this.animate(state.IDLE, JellyfishAnimations.IDLE, state.age);
    }
}
