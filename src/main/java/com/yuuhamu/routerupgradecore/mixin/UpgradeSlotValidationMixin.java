package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "me.desht.modularrouters.block.tile.ModularRouterBlockEntity$UpgradeHandler", remap = false)
public abstract class UpgradeSlotValidationMixin {

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void routerupgradecore$restrictModeMarkers(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!ModeRegistry.isRegisteredMarker(stack.getItem())) {
            return;
        }
        IItemHandler self = (IItemHandler) (Object) this;
        for (int i = 0; i < self.getSlots(); i++) {
            if (i == slot) {
                continue;
            }
            ItemStack existing = self.getStackInSlot(i);
            if (!existing.isEmpty() && ModeRegistry.isRegisteredMarker(existing.getItem())) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}

