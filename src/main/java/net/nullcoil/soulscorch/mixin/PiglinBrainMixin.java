package net.nullcoil.soulscorch.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PiglinBrain;
import net.nullcoil.soulscorch.entity.ModEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "isZombified", at = @At("HEAD"), cancellable = true)
    private static void isZombified(EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
        if (entityType == ModEntities.SOULLESS) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
