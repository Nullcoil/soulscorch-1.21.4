package net.nullcoil.soulscorch.entity.client.soulless;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class SoullessRenderState extends BipedEntityRenderState {
    /** Determines the state of the Soulless */
    public SoullessActivity currentActivity;

    public final AnimationState passiveAnimationState = new AnimationState();
    public final AnimationState neutralAnimationState = new AnimationState();
    public final AnimationState hostileAnimationState = new AnimationState();
}