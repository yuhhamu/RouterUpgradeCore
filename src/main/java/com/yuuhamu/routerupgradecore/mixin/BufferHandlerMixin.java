package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.container.handler.BufferHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BufferHandler.class, remap = false)
public abstract class BufferHandlerMixin extends ItemStackHandler {

    @Shadow
    @Final
    private ModularRouterBlockEntity router;

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (ModeRegistry.getActiveProvider(this.router) != null) {
            return false;
        }
        return super.isItemValid(slot, stack);
    }
}
