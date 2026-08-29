package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.item.module.TargetedModule;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TargetedModule.class, remap = false)
public abstract class TargetedModuleMixin {

    @Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
    private void routerupgradecore$isValidTarget(UseOnContext ctx, CallbackInfoReturnable<Boolean> cir) {
        Item moduleItem = (Item) (Object) this;
        if (ModeRegistry.shouldRelaxTarget(moduleItem, ctx)) {
            cir.setReturnValue(!ctx.getLevel().isEmptyBlock(ctx.getClickedPos()));
        }
    }
}
