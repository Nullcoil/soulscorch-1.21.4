package net.nullcoil.soulscorch.entity.client.soulless;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

import java.util.Random;

public class SoullessAnimations {
    private static final Random RANDOM = new Random();

    public static final Animation PASSIVE = Animation.Builder.create(0.08333f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.04167f, AnimationHelper.createRotationalVector(0f, 0f, 17.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("arms",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.04167f, AnimationHelper.createRotationalVector(0f, -12.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation NEUTRAL_HEAD_TWITCH0 = Animation.Builder.create(0.08343333f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, 17.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation NEUTRAL_HEAD_TWITCH1 = Animation.Builder.create(0.125f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(5f, -22.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(5f, -22.5f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation NEUTRAL_ARM_TWITCH = Animation.Builder.create(0.16766666f)
            .addBoneAnimation("leftArm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(-10f, -22.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(-10f, -22.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.16766666f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    private static final Animation[] NEUTRAL_ANIMATIONS = new Animation[]{
            NEUTRAL_HEAD_TWITCH0,
            NEUTRAL_HEAD_TWITCH1,
            NEUTRAL_ARM_TWITCH
    };

    public static Animation NEUTRAL() {
        int index = RANDOM.nextInt(NEUTRAL_ANIMATIONS.length);
        return NEUTRAL_ANIMATIONS[index];
    }
}