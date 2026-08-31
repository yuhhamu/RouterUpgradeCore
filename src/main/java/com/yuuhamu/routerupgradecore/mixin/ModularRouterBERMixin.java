package com.yuuhamu.routerupgradecore.mixin;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.render.blockentity.ModularRouterBER;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModularRouterBER.class, remap = false)
public abstract class ModularRouterBERMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;getCamouflage()Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState routerupgradecore$realCamouflageForHighlightOnly(ModularRouterBlockEntity router) {
        return ((ModularRouterCamouflageAccessor) router).routerupgradecore$getRealCamouflage();
    }
}

