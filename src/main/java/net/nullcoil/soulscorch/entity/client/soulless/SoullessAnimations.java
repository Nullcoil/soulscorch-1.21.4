package net.nullcoil.soulscorch.entity.client.soulless;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

import java.util.Random;

public class SoullessAnimations {
    private static final Random RANDOM = new Random();
    private static int counter = 0;
    private static int twitchTicksRemaining = 0;

    public static final Animation BLANK = Animation.Builder.create(10f).looping().build();
    public static final Animation PASSIVE = Animation.Builder.create(0.25f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, 17.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, -12.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.041676664f, AnimationHelper.createRotationalVector(0f, 0f, -12.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.08343333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
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
            .addBoneAnimation("left_arm",
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

    public static Animation PASSIVE() {
        if (twitchTicksRemaining > 0) {
            twitchTicksRemaining--;
            return PASSIVE; // enforce minimum duration
        }

        float r = RANDOM.nextFloat();
        if (counter >= 600) { // 100 ticks = 5 seconds at 20 TPS
            counter = 0;
            if (r <= 0.2f) {
                twitchTicksRemaining = 5; // 5 ticks = 0.25 seconds
                System.out.println("TWITCHED. I TWITCHED. DID YOU SEE IT?");
                return PASSIVE;
            }
        }

        counter++;
        return BLANK;
    }

    public static Animation NEUTRAL() {
        float r = RANDOM.nextFloat();
        if(r < 0.8f) {
            return BLANK;
        } else {
            int index = RANDOM.nextInt(NEUTRAL_ANIMATIONS.length);
            return NEUTRAL_ANIMATIONS[index];
        }
    }
}