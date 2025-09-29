package net.nullcoil.soulscorch.entity.client.restless;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BabyModelTransformer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.nullcoil.soulscorch.Soulscorch;

import java.util.Set;

public class RestlessModel extends EntityModel<RestlessRenderState> {
    public static final EntityModelLayer RESTLESS = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "restless"), "main");
    public static final ModelTransformer BABY_TRANSFORMER = new BabyModelTransformer(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F, Set.of("head"));
    private static final float HEAD_PITCH_START = 0.87266463F;
    private static final float HEAD_PITCH_END = -0.34906584F;
    private final ModelPart head;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart body;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart mane;

    public RestlessModel(ModelPart modelPart) {
        super(modelPart);
        this.body = modelPart.getChild("body");
        this.mane = this.body.getChild("mane");
        this.head = modelPart.getChild("head");
        this.rightEar = this.head.getChild("right_ear");
        this.leftEar = this.head.getChild("left_ear");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.rightHindLeg = modelPart.getChild("right_hind_leg");
        this.leftHindLeg = modelPart.getChild("left_hind_leg");
    }

    private static ModelData getModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("body", ModelPartBuilder.create().uv(1, 1).cuboid(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F), ModelTransform.pivot(0.0F, 7.0F, 0.0F));
        modelPartData2.addChild("mane", ModelPartBuilder.create().uv(90, 33).cuboid(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -14.0F, -7.0F));
        ModelPartData modelPartData3 = modelPartData.addChild("head", ModelPartBuilder.create().uv(61, 1).cuboid(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F), ModelTransform.of(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
        modelPartData3.addChild("right_ear", ModelPartBuilder.create().uv(1, 1).cuboid(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.6981317F));
        modelPartData3.addChild("left_ear", ModelPartBuilder.create().uv(1, 6).cuboid(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), ModelTransform.of(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.6981317F));
        modelPartData3.addChild("right_horn", ModelPartBuilder.create().uv(10, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(-7.0F, 2.0F, -12.0F));
        modelPartData3.addChild("left_horn", ModelPartBuilder.create().uv(1, 13).cuboid(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), ModelTransform.pivot(7.0F, 2.0F, -12.0F));
        int i = 14;
        int j = 11;
        modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(66, 42).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), ModelTransform.pivot(-4.0F, 10.0F, -8.5F));
        modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(41, 42).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), ModelTransform.pivot(4.0F, 10.0F, -8.5F));
        modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(21, 45).cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), ModelTransform.pivot(-5.0F, 13.0F, 10.0F));
        modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(0, 45).cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), ModelTransform.pivot(5.0F, 13.0F, 10.0F));
        return modelData;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = getModelData();
        return TexturedModelData.of(modelData, 128, 64);
    }

    public static TexturedModelData getBabyTexturedModelData() {
        ModelData modelData = getModelData();
        ModelPartData modelPartData = modelData.getRoot().getChild("body");
        modelPartData.addChild("mane", ModelPartBuilder.create().uv(90, 33).cuboid(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -14.0F, -3.0F));
        return TexturedModelData.of(modelData, 128, 64).transform(BABY_TRANSFORMER);
    }

    public void setAngles(RestlessRenderState restlessRenderState) {
        super.setAngles(restlessRenderState);
        float f = restlessRenderState.limbAmplitudeMultiplier;
        float g = restlessRenderState.limbFrequency;
        this.rightEar.roll = -0.6981317F - f * MathHelper.sin(g);
        this.leftEar.roll = 0.6981317F + f * MathHelper.sin(g);
        this.head.yaw = restlessRenderState.yawDegrees * ((float)Math.PI / 180F);
        float h = 1.0F - (float)MathHelper.abs(10 - 2 * restlessRenderState.movementCooldownTicks) / 10.0F;
        this.head.pitch = MathHelper.lerp(h, 0.87266463F, -0.34906584F);
        if (restlessRenderState.baby) {
            ModelPart var10000 = this.head;
            var10000.pivotY += h * 2.5F;
        }

        float i = 1.2F;
        this.rightFrontLeg.pitch = MathHelper.cos(g) * 1.2F * f;
        this.leftFrontLeg.pitch = MathHelper.cos(g + (float)Math.PI) * 1.2F * f;
        this.rightHindLeg.pitch = this.leftFrontLeg.pitch;
        this.leftHindLeg.pitch = this.rightFrontLeg.pitch;
    }
}
