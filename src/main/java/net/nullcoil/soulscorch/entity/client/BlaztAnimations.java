package net.nullcoil.soulscorch.entity.client;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class BlaztAnimations {

    public static final Animation IDLE = Animation.Builder.create(2f).looping()
            .addBoneAnimation("inner",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, -360f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("outer",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 360f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation AGGRO = Animation.Builder.create(1f).looping()
            .addBoneAnimation("inner",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 360f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("outer",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 360f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
}
