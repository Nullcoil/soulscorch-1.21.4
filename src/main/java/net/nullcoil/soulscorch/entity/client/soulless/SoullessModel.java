package net.nullcoil.soulscorch.entity.client.soulless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PiglinBaseEntityModel;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;
import org.joml.Vector3f;


public class SoullessModel extends PiglinBaseEntityModel<SoullessRenderState> {
    public static final EntityModelLayer SOULLESS = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID,"soulless"),"main");


    @Environment(EnvType.CLIENT)
    public SoullessModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(getTexturedModelData(Dilation.NONE), 64,64);
    }

    public static ModelData getTexturedModelData(Dilation dilation) {
        return getModelData(dilation);
    }

    public void setAngles(SoullessRenderState state) {
        super.setAngles(state);
        this.resetTransforms();

        switch (state.currentActivity) {
            case PASSIVE -> {
                this.body.pitch = (float)Math.toRadians(20);

                this.head.pitch = (float)Math.toRadians(20);
                this.head.yaw = (float)Math.toRadians(-12);
                this.head.roll = (float)Math.toRadians(-4);

                this.leftArm.pitch = (float)Math.toRadians(-20);
                this.rightArm.pitch = (float)Math.toRadians(-20);

                this.leftLeg.pivotZ += 4;
                this.rightLeg.pivotZ += 4;

                this.leftLeg.pitch = (float)Math.toRadians(10);
                this.leftLeg.roll = (float)Math.toRadians(-5);

                this.rightLeg.pitch = (float)Math.toRadians(-10);
                this.rightLeg.yaw = (float)Math.toRadians(-7);
                this.rightLeg.roll = (float)Math.toRadians(5);

                // Apply passive animation
                this.animate(state.passiveAnimationState, SoullessAnimations.PASSIVE(),state.age);
            }
            case NEUTRAL -> {
                this.body.pitch = (float)Math.toRadians(-17.5);

                this.head.pitch = (float)Math.toRadians(14.6599);
                this.head.yaw = (float)Math.toRadians(3.2113);
                this.head.roll = (float)Math.toRadians(-12.0868);

                this.leftArm.pitch = (float)Math.toRadians(15);
                this.rightArm.pitch = (float)Math.toRadians(15);

                this.animate(state.neutralAnimationState, SoullessAnimations.NEUTRAL(),state.age);
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
        this.jacket.copyTransform(this.body);
    }
}