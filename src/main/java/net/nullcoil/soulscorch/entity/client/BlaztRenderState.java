package net.nullcoil.soulscorch.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class BlaztRenderState extends LivingEntityRenderState {
    public boolean shooting;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
}