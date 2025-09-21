package net.nullcoil.soulscorch.entity.client.soulless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;
import org.joml.Vector3f;


public class SoullessModel extends BipedEntityModel<SoullessRenderState> {
    public static final EntityModelLayer SOULLESS = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID,"soulless"),"main");
    private final ModelPart upperBody;

    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;

    private final ModelPart arms;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart body;

    private final ModelPart hat;
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;


    @Environment(EnvType.CLIENT)
    public SoullessModel(ModelPart root) {
        super(root);

        // Get custom parts
        this.upperBody = root.getChild("upper_body");
        this.arms = upperBody.getChild("arms");

        // Get standard biped parts
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");

        // Get ears from head
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");

        // Get clothing parts
        this.hat = this.head.getChild("hat");
        this.leftSleeve = this.leftArm.getChild("left_sleeve");
        this.rightSleeve = this.rightArm.getChild("right_sleeve");
        this.leftPants = this.leftLeg.getChild("left_pants");
        this.rightPants = this.rightLeg.getChild("right_pants");
        this.jacket = this.body.getChild("jacket");
    }

    public static TexturedModelData getTexturedModelData() {
        return createModelData(Dilation.NONE);
    }

    public static TexturedModelData createModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // Create the standard biped structure first
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create()
                        .uv(0,0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, dilation)
                        .uv(32,0).cuboid(-4f, -8f, -4f, 8f, 8f, 8f, dilation.add(0.5f)),
                ModelTransform.pivot(0f, 0f, 0f));

        ModelPartData hat = head.addChild("hat", ModelPartBuilder.create(),
                ModelTransform.NONE);

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create()
                        .uv(16,16).cuboid(-4f, 0f, -2f, 8f, 12f, 4f, dilation)
                        .uv(16,32).cuboid(-4f, 0f, -2f, 8f, 12f, 4f, dilation.add(0.25f)),
                ModelTransform.pivot(0f, 0f, 0f));

        ModelPartData jacket = body.addChild("jacket", ModelPartBuilder.create(),
                ModelTransform.NONE);

        ModelPartData rightArm = modelPartData.addChild("right_arm", ModelPartBuilder.create()
                        .uv(40,16).cuboid(-3f, -2f, -2f, 4f, 12f, 4f, dilation)
                        .uv(40,32).cuboid(-3f, -2f, -2f, 4f, 12f, 4f, dilation.add(0.25f)),
                ModelTransform.pivot(-5f, 2f, 0f));

        ModelPartData rightSleeve = rightArm.addChild("right_sleeve", ModelPartBuilder.create(),
                ModelTransform.NONE);

        ModelPartData leftArm = modelPartData.addChild("left_arm", ModelPartBuilder.create()
                        .uv(32,48).cuboid(-1f, -2f, -2f, 4f, 12f, 4f, dilation)
                        .uv(48,48).cuboid(-1f, -2f, -2f, 4f, 12f, 4f, dilation.add(0.25f)),
                ModelTransform.pivot(5f, 2f, 0f));

        ModelPartData leftSleeve = leftArm.addChild("left_sleeve", ModelPartBuilder.create(),
                ModelTransform.NONE);

        ModelPartData rightLeg = modelPartData.addChild("right_leg", ModelPartBuilder.create()
                        .uv(0,16).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, dilation)
                        .uv(0,32).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, dilation.add(0.25f)),
                ModelTransform.pivot(-1.9f, 12f, 0f));

        ModelPartData rightPants = rightLeg.addChild("right_pants", ModelPartBuilder.create(),
                ModelTransform.NONE);

        ModelPartData leftLeg = modelPartData.addChild("left_leg", ModelPartBuilder.create()
                        .uv(16,48).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, dilation)
                        .uv(0,48).cuboid(-2f, 0f, -2f, 4f, 12f, 4f, dilation.add(0.25f)),
                ModelTransform.pivot(1.9f, 12f, 0f));

        ModelPartData leftPants = leftLeg.addChild("left_pants", ModelPartBuilder.create(),
                ModelTransform.NONE);

        // Custom parts for the soulless entity
        ModelPartData upper_body = modelPartData.addChild("upper_body", ModelPartBuilder.create(),
                ModelTransform.pivot(0f, 0f, 0f));

        ModelPartData arms = upper_body.addChild("arms", ModelPartBuilder.create(),
                ModelTransform.pivot(0f, 2f, 0f));

        // Add ears to the head
        ModelPartData leftEar = head.addChild("left_ear", ModelPartBuilder.create()
                        .uv(24,0).cuboid(-0.5f, -3f, -2f, 1f, 5f, 4f, dilation),
                ModelTransform.of(-4f, -6f, 0f, 0f, 0f, (float)Math.toRadians(-22.5f)));

        ModelPartData rightEar = head.addChild("right_ear", ModelPartBuilder.create()
                        .uv(24,0).cuboid(-0.5f, -3f, -2f, 1f, 5f, 4f, dilation),
                ModelTransform.of(4f, -6f, 0f, 0f, 0f, (float)Math.toRadians(22.5f)));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public void setAngles(SoullessRenderState state) {
        super.setAngles(state);

        // Reset to base pose first
        this.resetTransforms();

        switch (state.currentActivity) {
            case PASSIVE -> {
                this.upperBody.pitch = (float)Math.toRadians(-20);

                this.head.pitch = (float)Math.toRadians(-20);
                this.head.yaw = (float)Math.toRadians(12);
                this.head.roll = (float)Math.toRadians(4);

                this.leftArm.pitch = (float)Math.toRadians(20);
                this.rightArm.pitch = (float)Math.toRadians(20);

                this.leftLeg.pivotX = -1.9f;
                this.leftLeg.pivotY = 12f;
                this.leftLeg.pitch = (float)Math.toRadians(10);
                this.leftLeg.roll = (float)Math.toRadians(-5);

                this.rightLeg.pivotX = 1.9f;
                this.rightLeg.pivotY = 12f;
                this.rightLeg.pitch = (float)Math.toRadians(-10);
                this.rightLeg.yaw = (float)Math.toRadians(-7);
                this.rightLeg.roll = (float)Math.toRadians(5);

                // Apply passive animation
                AnimationHelper.animate(this, SoullessAnimations.PASSIVE, state.passiveAnimationState.getTimeInMilliseconds(state.age), 1.0f, new Vector3f());
            }
            case NEUTRAL -> {
                this.upperBody.pitch = (float)Math.toRadians(-17.5);

                this.head.pitch = (float)Math.toRadians(14.6599);
                this.head.yaw = (float)Math.toRadians(3.2113);
                this.head.roll = (float)Math.toRadians(-12.0868);

                this.leftArm.pitch = (float)Math.toRadians(15);
                this.rightArm.pitch = (float)Math.toRadians(15);

                // Apply random neutral animation when animation state is running
                if (state.neutralAnimationState.isRunning()) {
                    AnimationHelper.animate(this, SoullessAnimations.NEUTRAL(), state.neutralAnimationState.getTimeInMilliseconds(state.age), 1.0f, new Vector3f());
                }
            }
            case HOSTILE -> {
                CrossbowPosing.meleeAttack(this.leftArm, this.rightArm, true, state.handSwingProgress, state.age);

                // You can add hostile-specific animations here if needed
                // AnimationHelper.animate(this, SoullessAnimations.HOSTILE_ANIMATION, state.hostileAnimationState.getTimeRunning(state.age), 1.0f);
            }
        }

        this.leftSleeve.copyTransform(this.leftArm);
        this.rightSleeve.copyTransform(this.rightArm);
        this.leftPants.copyTransform(this.leftLeg);
        this.rightPants.copyTransform(this.rightLeg);
        this.jacket.copyTransform(this.upperBody);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.leftSleeve.visible = visible;
        this.rightSleeve.visible = visible;
        this.leftPants.visible = visible;
        this.rightPants.visible = visible;
        this.jacket.visible = visible;
    }
}