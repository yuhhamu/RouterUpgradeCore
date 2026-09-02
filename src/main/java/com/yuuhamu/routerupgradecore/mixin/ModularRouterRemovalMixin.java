package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * NeoForge版ModularRouterBlockEntityは{@code setRemoved()}を独自にオーバーライドしていない
 * (javap実測で確認済み)ため、vanilla基底クラスであるBlockEntity自体へMixinし、
 * instanceofで対象を絞り込む方式を取る。
 */
@Mixin(value = BlockEntity.class, remap = false)
public abstract class ModularRouterRemovalMixin {

    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void routerupgradecore$onSetRemoved(CallbackInfo ci) {
        if ((Object) this instanceof ModularRouterBlockEntity router) {
            RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
            if (provider != null) {
                provider.onRemoved(router);
            }
        }
    }
}
