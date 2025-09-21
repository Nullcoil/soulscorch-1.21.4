package net.nullcoil.soulscorch.entity.client.soulless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PiglinBaseEntityModel;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;


public class SoullessModel extends PiglinBaseEntityModel {
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
    public SoullessModel(ModelPart root, ModelPart hat, ModelPart leftSleeve, ModelPart rightSleeve, ModelPart leftPants, ModelPart rightPants, ModelPart jacket) {
        super(root);
        this.upperBody = root.getChild("upper_body");
        this.arms = upperBody.getChild("arms");
        // rewired biped parts
        this.head = this.upperBody.getChild("head");
        this.leftEar = this.head.getChild("left_ear");
        this.rightEar = this.head.getChild("right_ear");
        this.body = this.upperBody.getChild("body");
        this.rightArm = this.arms.getChild("right_arm");
        this.leftArm = this.arms.getChild("left_arm");

        this.hat = this.head.getChild("hat");
        this.leftSleeve = this.leftArm.getChild("left_sleeve");
        this.rightSleeve = this.rightArm.getChild("right_sleeve");
        this.leftPants = this.leftLeg.getChild("left_pants");
        this.rightPants = this.rightLeg.getChild("right_pants");
        this.jacket = this.body.getChild("jacket");
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData upper_body = modelPartData.addChild("upper_body", ModelPartBuilder.create(), ModelTransform.pivot(0,12,0));
        ModelPartData arms = upper_body.addChild("arms", ModelPartBuilder.create(), ModelTransform.pivot(0,22,0));

        ModelPartData head = upper_body.addChild("head", ModelPartBuilder.create()
                .uv(0,0).cuboid(-5f, -8f, -4f,10f,8f,8f, dilation)
                .uv(31,1).cuboid(-2f,-4f,-5f,4f,4f,1f,dilation)
                .uv(2,4).cuboid(2f,-2f,-5f,1f,2f,1f,dilation)
                .uv(2,0).cuboid(-3f,-2f,-5f,1f,2f,1f,dilation),
                ModelTransform.NONE);
        ModelPartData leftEar = head.addChild("leftEar", ModelPartBuilder.create()
                .uv(51,6).cuboid(-5f,-25f,-2f,1f,5f,4f,dilation),
                ModelTransform.of(-5f,30f,0f,0,0,(float)Math.toRadians(-22.5f)));
        ModelPartData rightEar = head.addChild("rightEar", ModelPartBuilder.create()
                .uv(39,6).cuboid(4f,25f,-2f,1f,5f,4f,dilation),
                ModelTransform.of(5,30,0,0,0,(float)Math.toRadians(22.5)));

        ModelPartData body = upper_body.addChild("body",ModelPartBuilder.create()
                .uv(16,16).cuboid(-4f,12f,-2f,8f,12f,4f,dilation),
                ModelTransform.pivot(0f,12f,0f));

        ModelPartData leftArm = upper_body.addChild("leftArm", ModelPartBuilder.create()
                .uv(32,48).cuboid(-8f,12f,-2f,4f,12f,4f,dilation),
                ModelTransform.pivot(-4f,22f,0f));
        ModelPartData rightArm = upper_body.addChild("rightArm", ModelPartBuilder.create()
                .uv(40,16).cuboid(4f,12f,-2f,4f,12f,4f,dilation),
                ModelTransform.pivot(4f,22f,0f));




        return TexturedModelData.of(modelData, 64,64);
    }

    public void setAngles(SoullessRenderState state) {
        super.setAngles(state);
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
            }
            case NEUTRAL -> {
                this.upperBody.pitch = (float)Math.toRadians(-17.5);

                this.head.pitch = (float)Math.toRadians(-14.1327);
                this.head.yaw = (float)Math.toRadians(5.0785);
                this.head.roll = (float)Math.toRadians(-19.3701);

                this.leftArm.pitch = (float)Math.toRadians(15);
                this.rightArm.pitch = (float)Math.toRadians(15);
            }
            case HOSTILE -> {
                CrossbowPosing.meleeAttack(this.leftArm, this.rightArm, true, state.handSwingProgress, state.age);
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

