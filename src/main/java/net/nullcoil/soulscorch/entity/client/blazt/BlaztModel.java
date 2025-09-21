package net.nullcoil.soulscorch.entity.client.blazt;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class BlaztModel extends EntityModel<BlaztRenderState> {
    public static final EntityModelLayer BLAZT = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "blazt"), "main");
    private final ModelPart tentacles;
    private final ModelPart bb_main;
    // Add direct references to the animated parts
    private final ModelPart inner;
    private final ModelPart outer;

    public BlaztModel(ModelPart root) {
        super(root);
        this.tentacles = root.getChild("tentacles");
        this.bb_main = root.getChild("bb_main");
        // Get direct references to the animated parts
        this.inner = this.tentacles.getChild("inner");
        this.outer = this.tentacles.getChild("outer");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData tentacles = modelPartData.addChild("tentacles", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData inner = tentacles.addChild("inner", ModelPartBuilder.create().uv(0, 0).cuboid(3.0F, -42.0F, -9.0F, 6.0F, 42.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-9.0F, -42.0F, -9.0F, 6.0F, 42.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(3.0F, -42.0F, 3.0F, 6.0F, 42.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-9.0F, -42.0F, 3.0F, 6.0F, 42.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData outer = tentacles.addChild("outer", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = outer.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -20.5F, -3.0F, 6.0F, 41.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, -4.5F, 11.0F, -0.2618F, 2.3562F, 0.0F));

        ModelPartData cube_r2 = outer.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -20.5F, -3.0F, 6.0F, 41.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -4.5F, 11.0F, -0.2618F, -2.3562F, 0.0F));

        ModelPartData cube_r3 = outer.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -20.5F, -3.0F, 6.0F, 41.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-11.0F, -4.5F, -11.0F, -0.2618F, 0.7854F, 0.0F));

        ModelPartData cube_r4 = outer.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -20.5F, -3.0F, 6.0F, 41.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -4.5F, -11.0F, -0.2618F, -0.7854F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-24.0F, -48.0F, -24.0F, 48.0F, 48.0F, 48.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 192, 96);
    }

    @Override
    public void setAngles(BlaztRenderState state) {
        super.setAngles(state);

        // Reset all transforms first
        this.getRootPart().traverse().forEach(ModelPart::resetTransform);

        // Apply animations using the AnimationState objects
        if (state.shooting) {
            // Use AGGRO animation when shooting
            this.animate(state.shootAnimationState, BlaztAnimations.AGGRO, state.age);
        } else {
            // Use IDLE animation when not shooting
            this.animate(state.idleAnimationState, BlaztAnimations.IDLE, state.age);
        }
    }
}