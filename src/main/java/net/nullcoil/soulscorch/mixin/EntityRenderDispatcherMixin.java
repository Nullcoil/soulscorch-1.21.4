package net.nullcoil.soulscorch.mixin;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.nullcoil.soulscorch.entity.custom.jellyfish.JellyfishEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderHitbox", at = @At("TAIL"))
    private static void onRenderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, float red, float green, float blue, CallbackInfo ci) {
        if (entity instanceof JellyfishEntity jellyfish) {
            double targetX = jellyfish.getClientTargetX();
            double targetY = jellyfish.getClientTargetY();
            double targetZ = jellyfish.getClientTargetZ();

            // Calculate vector from entity to target in WORLD space
            double vecX = targetX - entity.getX();
            double vecY = targetY - entity.getY();
            double vecZ = targetZ - entity.getZ();

            float verticalOffset = entity.getHeight() * 0.5f;

            VertexRendering.drawVector(matrices, vertices,
                    new Vector3f(0.0f, verticalOffset, 0.0f),
                    new Vec3d(vecX, vecY, vecZ),
                    0xFF00FF00);
        }
    }
}
