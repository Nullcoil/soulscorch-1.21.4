package net.nullcoil.soulscorch.entity.client.soulless;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.custom.SoullessEntity;

public class SoullessRenderer extends BipedEntityRenderer<SoullessEntity, SoullessRenderState, SoullessModel> {
    private static final Identifier TEXTURE = Identifier.of(Soulscorch.MOD_ID, "textures/entity/soulless/soulless.png");
    private static final Identifier AWAKENED =
            Identifier.of(Soulscorch.MOD_ID, "textures/entity/soulless/awakened.png");

    public SoullessRenderer(EntityRendererFactory.Context context, EntityModelLayer mainLayer, EntityModelLayer armorInnerLayer, EntityModelLayer armorOuterLayer) {
        super(context, new SoullessModel(context.getPart(mainLayer)),0f);
        super.addFeature(new ArmorFeatureRenderer(this, new ArmorEntityModel(context.getPart(armorInnerLayer)), new ArmorEntityModel(context.getPart(armorOuterLayer)), context.getEquipmentRenderer()));
    }

    @Override
    public SoullessRenderState createRenderState() {
        return new SoullessRenderState();
    }

    public void updateRenderState(SoullessEntity entity, SoullessRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.currentActivity = entity.getActivity();
        state.active = entity.getActivity() != SoullessActivity.PASSIVE;

        // Copy all animation states from entity to render state
        state.passiveAnimationState.copyFrom(entity.passiveAnimationState);
        state.neutralAnimationState.copyFrom(entity.neutralAnimationState);
        state.hostileAnimationState.copyFrom(entity.hostileAnimationState);
    }

    @Override
    public Identifier getTexture(SoullessRenderState state) {

        return state.active ? AWAKENED : TEXTURE;
    }
}