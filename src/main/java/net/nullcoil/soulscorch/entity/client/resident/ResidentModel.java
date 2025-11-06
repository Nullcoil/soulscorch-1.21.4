package net.nullcoil.soulscorch.entity.client.resident;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;

public class ResidentModel extends EntityModel<ResidentRenderState> {
    public static final EntityModelLayer RESIDENT = new EntityModelLayer(Identifier.of(Soulscorch.MOD_ID, "the_resident"), "main");

    private final ModelPart head;
    private final ModelPart l_arm;
    private final ModelPart r_arm;
    private final ModelPart legs;
    private final ModelPart front_legs;
    private final ModelPart fl_leg;
    private final ModelPart fr_leg;
    private final ModelPart back_legs;
    private final ModelPart bl_leg;
    private final ModelPart br_leg;
    private final ModelPart torso;
    private final ModelPart upper_torso;

    public ResidentModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.l_arm = root.getChild("l_arm");
        this.r_arm = root.getChild("r_arm");
        this.legs = root.getChild("legs");
        this.front_legs = this.legs.getChild("front_legs");
        this.fl_leg = this.front_legs.getChild("fl_leg");
        this.fr_leg = this.front_legs.getChild("fr_leg");
        this.back_legs = this.legs.getChild("back_legs");
        this.bl_leg = this.back_legs.getChild("bl_leg");
        this.br_leg = this.back_legs.getChild("br_leg");
        this.torso = root.getChild("torso");
        this.upper_torso = this.torso.getChild("upper_torso");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData
                .addChild("head", ModelPartBuilder
                                .create()
                                .uv(72, 28)
                                .cuboid(-2.0F, -10.0F, -1.0F, 8.0F, 13.0F, 8.0F,
                                        new Dilation(0.0F))
                                .uv(24, 72)
                                .cuboid(-2.0F, -10.0F, -1.0F,
                                        8.0F, 16.0F, 8.0F,
                                        new Dilation(0.0F)),
                        ModelTransform.pivot(-1.0F, -25.0F, -13.0F));

        ModelPartData l_arm = modelPartData
                .addChild("l_arm",
                        ModelPartBuilder.create()
                                .uv(48, 28)
                                .cuboid(-1.0F, -20.0F, -1.0F,
                                        6.0F, 38.0F, 6.0F,
                                        new Dilation(0.0F)),
                        ModelTransform.pivot(13.0F, -6.0F, -5.0F));

        ModelPartData r_arm = modelPartData
                .addChild("r_arm",
                        ModelPartBuilder.create()
                                .uv(0, 53)
                                .cuboid(0.0F, -24.0F, -1.0F,
                                        6.0F, 43.0F, 6.0F,
                                        new Dilation(0.0F)),
                        ModelTransform.pivot(-17.0F, -6.0F, -5.0F));

        ModelPartData legs = modelPartData
                .addChild("legs", ModelPartBuilder.create(),
                        ModelTransform.pivot(3.0F, 24.0F, -5.0F));

        ModelPartData front_legs = legs.addChild("front_legs",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData fl_leg = front_legs.addChild("fl_leg",
                ModelPartBuilder.create()
                        .uv(74, 0)
                        .cuboid(-1.0F, -17.0F, -1.0F,
                                4.0F, 17.0F, 4.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData fr_leg = front_legs.addChild("fr_leg",
                ModelPartBuilder.create()
                        .uv(88, 69)
                        .cuboid(-1.0F, -17.0F, -1.0F,
                                4.0F, 17.0F, 4.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(-8.0F, 0.0F, 0.0F));

        ModelPartData back_legs = legs.addChild("back_legs",
                ModelPartBuilder.create(),
                ModelTransform.pivot(2.0F, 0.0F, 12.0F));

        ModelPartData bl_leg = back_legs.addChild("bl_leg",
                ModelPartBuilder.create()
                        .uv(56, 72)
                        .cuboid(-1.0F, -28.0F, -1.0F,
                                4.0F, 28.0F, 4.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData br_leg = back_legs.addChild("br_leg",
                ModelPartBuilder.create()
                        .uv(72, 69)
                        .cuboid(-1.0F, -28.0F, -1.0F,
                                4.0F, 28.0F, 4.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(-12.0F, 0.0F, 0.0F));

        ModelPartData torso = modelPartData.addChild("torso",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, -1.0F, 2.0F));

        ModelPartData pelvis_r1 = torso.addChild("pelvis_r1",
                ModelPartBuilder.create()
                        .uv(72, 49)
                        .cuboid(-4.0F, -3.0F, -5.0F,
                                8.0F, 12.0F, 8.0F,
                                new Dilation(0.0F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F,
                        -0.2182F, 0.0F, 0.0F));

        ModelPartData upper_torso = torso.addChild("upper_torso",
                ModelPartBuilder.create(),
                ModelTransform.pivot(4.0F, -3.0F, -3.0F));

        ModelPartData body_r1 = upper_torso.addChild("body_r1",
                ModelPartBuilder.create()
                        .uv(0, 28)
                        .cuboid(-11.0F, -15.0F, -1.0F,
                                14.0F, 15.0F, 10.0F,
                                new Dilation(0.0F)),
                ModelTransform.of(0.0F, 0.0F, 0.0F,
                        -0.0865F, 0.0066F, 0.0093F));

        ModelPartData shoulders_r1 = upper_torso.addChild("shoulders_r1",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-13.0F, 0.0F, -3.0F,
                                24.0F, 15.0F, 13.0F,
                                new Dilation(0.0F)),
                ModelTransform.of(-2.0F, -24.0F, -3.0F,
                        0.3043F, -0.0262F, 0.0832F));

        return TexturedModelData.of(modelData, 104, 104);
    }
}