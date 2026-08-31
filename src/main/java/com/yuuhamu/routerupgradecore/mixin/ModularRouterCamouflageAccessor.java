package com.yuuhamu.routerupgradecore.mixin;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ModularRouterBlockEntity.class, remap = false)
public interface ModularRouterCamouflageAccessor {

    @Accessor("camouflage")
    BlockState routerupgradecore$getRealCamouflage();
}

