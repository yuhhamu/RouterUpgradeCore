package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public final class RouterUpgradeCore {

    private RouterUpgradeCore() {
    }

    public static void registerMode(Item markerUpgradeItem, RouterModeProvider provider, int imageColor) {
        ModeRegistry.register(markerUpgradeItem, provider, imageColor);
    }

    public static int getImageColor(Item markerUpgradeItem) {
        return ModeRegistry.getImageColor(markerUpgradeItem);
    }

    public static RouterModeProvider getActiveProvider(ModularRouterBlockEntity router) {
        return ModeRegistry.getActiveProvider(router);
    }

    @SuppressWarnings("unchecked")
    public static <T> LazyOptional<T> getActiveCapability(ModularRouterBlockEntity router, Capability<T> capability, Direction side) {
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return LazyOptional.empty();
        }
        return (LazyOptional<T>) provider.getCapability(router, capability, side, LazyOptional.empty());
    }
}

