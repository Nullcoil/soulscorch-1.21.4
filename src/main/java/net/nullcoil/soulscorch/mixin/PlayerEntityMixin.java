package net.nullcoil.soulscorch.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.nullcoil.soulscorch.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void soulscorch$modifyHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        if(self.hasStatusEffect(ModEffects.SOULSCORCH)) {
            cir.setReturnValue(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE);
        }
    }
}
