package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.internal.BeamPulseRegistry;
import com.yuuhamu.routerupgradecore.internal.BeamContinuityRegistry;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.util.BeamData;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.BlockCapability;

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

    public static void markBeamNoPulse(BeamData beam) {
        BeamPulseRegistry.markNoPulse(beam);
    }

    public static void reportBeamActive(ModularRouterBlockEntity router, Object beamKey,
                                         Runnable startAction, Runnable stopAction) {
        BeamContinuityRegistry.reportActive(router, beamKey, startAction, stopAction);
    }

    /**
     * 現在アクティブなRouterModeProviderへcapability要求を委譲する。
     * アドオンMod(FluidRouterUpgrade等)は自身の{@code RegisterCapabilitiesEvent}リスナーから
     * このメソッドを呼び出し、ModularRoutersのRouter BlockEntityType向けにcapabilityを登録すること。
     */
    public static <T> T getActiveCapability(ModularRouterBlockEntity router,
                                             BlockCapability<T, Direction> capability,
                                             Direction side) {
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return null;
        }
        return provider.getCapability(router, capability, side);
    }
}
