package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.internal.BeamPulseRegistry;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.util.BeamData;
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

    /**
     * 指定したBeamDataについて、Vanilla本体(ModularRouterBER)による1秒周期の
     * アルファ点滅を適用せず、常に一定の明るさで描画されるようにする。
     * FluidRouterUpgrade等の具体実装が、router.addItemBeam(BeamData)を呼ぶ前に
     * (reverseItems()/fadeItems()等の変換を全て適用し終えた、実際に渡す最終的な
     * インスタンスに対して)このメソッドで登録することを想定している。
     */
    public static void markBeamNoPulse(BeamData beam) {
        BeamPulseRegistry.markNoPulse(beam);
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

