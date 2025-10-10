package net.nullcoil.soulscorch.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.nullcoil.soulscorch.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$FuelSlot")
public class FuelSlotMixin {
    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private void soulCanInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(ModItems.BLAZT_POWDER)) {
            cir.setReturnValue(true);
        }
    }
}


