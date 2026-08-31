package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.internal.BeamPulseRegistry;
import com.yuuhamu.routerupgradecore.internal.BeamContinuityRegistry;
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

    /**
     * 稼働タイミング(executeModules呼び出し)ごとに、指定したbeamKeyで表される
     * 視覚効果(ビーム等)が輸送に成功して継続していることを報告する。
     *
     * 直前の稼働タイミングまでこのbeamKeyが報告されていなかった(=新規開始)場合のみ
     * startActionを1回呼び出す。既に継続中の場合は何もしない(表示をそのまま維持)。
     * その後、輸送が行われなかった稼働タイミングが来た時点(このメソッドが呼ばれ
     * なかった時点)で、開始時に渡したstopActionが自動的に1回だけ呼び出される。
     *
     * beamKeyはRouterModeProvider実装側が用意する、視覚効果を一意に識別できる
     * 任意のオブジェクト(equals/hashCodeが適切に実装されたもの、例えばrecord)を
     * 渡すこと。
     */
    public static void reportBeamActive(ModularRouterBlockEntity router, Object beamKey,
                                         Runnable startAction, Runnable stopAction) {
        BeamContinuityRegistry.reportActive(router, beamKey, startAction, stopAction);
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

