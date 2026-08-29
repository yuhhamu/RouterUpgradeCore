package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import net.minecraft.world.item.Item;

public final class RouterUpgradeCore {

    private RouterUpgradeCore() {
    }

    public static void registerMode(Item markerUpgradeItem, RouterModeProvider provider) {
        ModeRegistry.register(markerUpgradeItem, provider);
    }
}
