package com.yuuhamu.routerupgradecore.internal;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.items.IItemHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class ModeRegistry {

    private record ModeEntry(RouterModeProvider provider, int imageColor) {
    }

    private static final Map<Item, ModeEntry> PROVIDERS = new LinkedHashMap<>();

    private ModeRegistry() {
    }

    public static void register(Item markerUpgradeItem, RouterModeProvider provider, int imageColor) {
        PROVIDERS.put(markerUpgradeItem, new ModeEntry(provider, imageColor));
    }

    public static boolean isRegisteredMarker(Item item) {
        return PROVIDERS.containsKey(item);
    }

    public static RouterModeProvider getActiveProvider(ModularRouterBlockEntity router) {
        Item marker = resolveMarkerItem(router);
        if (marker == null) {
            return null;
        }
        ModeEntry entry = PROVIDERS.get(marker);
        return entry == null ? null : entry.provider();
    }

    public static Item getActiveMarkerItem(ModularRouterBlockEntity router) {
        return resolveMarkerItem(router);
    }

    public static Integer getActiveImageColor(ModularRouterBlockEntity router) {
        Item marker = resolveMarkerItem(router);
        if (marker == null) {
            return null;
        }
        ModeEntry entry = PROVIDERS.get(marker);
        return entry == null ? null : entry.imageColor();
    }

    public static int getImageColor(Item markerUpgradeItem) {
        ModeEntry entry = PROVIDERS.get(markerUpgradeItem);
        if (entry == null) {
            throw new IllegalArgumentException(
                    "markerUpgradeItem is not registered via RouterUpgradeCore.registerMode(): " + markerUpgradeItem);
        }
        return entry.imageColor();
    }

    public static boolean shouldRelaxTarget(Item moduleItem, UseOnContext context) {
        for (ModeEntry entry : PROVIDERS.values()) {
            if (entry.provider().relaxTargetValidation(moduleItem, context)) {
                return true;
            }
        }
        return false;
    }

    public static final String CLIENT_SYNC_MARKER_NBT_KEY = "RouterUpgradeCoreActiveMarker";

    private static final Map<ModularRouterBlockEntity, Item> CLIENT_SYNCED_MARKER = new WeakHashMap<>();

    public static void recordClientSyncedMarker(ModularRouterBlockEntity router, Item markerItemOrNull) {
        CLIENT_SYNCED_MARKER.put(router, markerItemOrNull);
    }

    private static Item resolveMarkerItem(ModularRouterBlockEntity router) {
        if (CLIENT_SYNCED_MARKER.containsKey(router)) {
            return CLIENT_SYNCED_MARKER.get(router);
        }
        return scanMarkerItem(router);
    }

    private static Item scanMarkerItem(ModularRouterBlockEntity router) {
        // NOTE: router.getUpgradeCount()はVanilla本体のcompileUpgrades()が構築するキャッシュに
        // 依存しており、compileUpgrades()自体はNBTロード直後ではなく次のtickで初めて実行される
        // (recompileNeededフラグの遅延コンパイル方式)。そのため load() 直後にgetUpgradeCount()を
        // 参照すると常に0が返り、実際にはUpgradeが挿入済みでもアクティブなProviderが見つからず、
        // リログイン直後にタンク等の状態復元が一切行われない不具合の原因になっていた。
        // upgradesHandlerの生スロットを直接走査することで、このタイミング依存を回避する。
        IItemHandler upgrades = router.getUpgrades();
        for (int i = 0; i < upgrades.getSlots(); i++) {
            ItemStack stack = upgrades.getStackInSlot(i);
            if (!stack.isEmpty() && PROVIDERS.containsKey(stack.getItem())) {
                return stack.getItem();
            }
        }
        return null;
    }
}

