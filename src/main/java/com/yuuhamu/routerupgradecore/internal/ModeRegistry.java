package com.yuuhamu.routerupgradecore.internal;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModeRegistry {

    private static final Map<Item, RouterModeProvider> PROVIDERS = new LinkedHashMap<>();

    private ModeRegistry() {
    }

    public static void register(Item markerUpgradeItem, RouterModeProvider provider) {
        PROVIDERS.put(markerUpgradeItem, provider);
    }

    public static boolean isRegisteredMarker(Item item) {
        return PROVIDERS.containsKey(item);
    }

    public static RouterModeProvider getActiveProvider(ModularRouterBlockEntity router) {
        for (Map.Entry<Item, RouterModeProvider> entry : PROVIDERS.entrySet()) {
            if (router.getUpgradeCount(entry.getKey()) > 0) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static boolean shouldRelaxTarget(Item moduleItem, UseOnContext context) {
        for (RouterModeProvider provider : PROVIDERS.values()) {
            if (provider.relaxTargetValidation(moduleItem, context)) {
                return true;
            }
        }
        return false;
    }
}
