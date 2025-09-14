package net.nullcoil.soulscorch.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.BlaztEntity;

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

        ModelPartData inner = tentacles.addChild("inner", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -17.0F, -8.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-8.0F, -17.0F, 4.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(4.0F, -17.0F, 4.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(4.0F, -17.0F, -8.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer = tentacles.addChild("outer", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -23.0F, 0.0F));

        ModelPartData cube_r1 = outer.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(7.9993F, 11.9322F, -7.1145F, -0.2618F, -0.7854F, 0.0F));

        ModelPartData cube_r2 = outer.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, 0.229F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(5.9993F, 11.9322F, 5.8855F, 0.2618F, 0.7854F, 0.0F));

        ModelPartData cube_r3 = outer.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, 0.229F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0007F, 11.9322F, 5.8855F, 0.2618F, -0.7854F, 0.0F));

        ModelPartData cube_r4 = outer.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 28.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-8.0007F, 11.9322F, -7.1145F, -0.2618F, 0.7854F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(BlaztRenderState state) {
        super.setAngles(state);

        // Reset transforms
        this.inner.resetTransform();
        this.outer.resetTransform();
        this.bb_main.resetTransform();

        // Debug output (keep for now)
        if (state.age % 40 == 0) { // Every 2 seconds
            System.out.println("Animation Debug - Shooting: " + state.shooting +
                    ", IdleRunning: " + state.idleAnimationState.isRunning() +
                    ", ShootRunning: " + state.shootAnimationState.isRunning());
        }

        // Apply animations - the animation system should work now
        if (state.shooting) {
            float time = state.age * 0.3f; // Slow rotation for testing
            this.inner.yaw = time;
            this.outer.yaw = -time;
        } else {
            float time = state.age * 0.1f; // Slow rotation for testing
            this.inner.yaw = time;
            this.outer.yaw = -time;
        }
    }
}