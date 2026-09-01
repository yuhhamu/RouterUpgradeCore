package com.yuuhamu.routerupgradecore.internal;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class BeamContinuityRegistry {

    private static final class RouterState {
        final Map<Object, Runnable> confirmedActive = new HashMap<>();
        final Map<Object, Runnable> pendingThisTick = new HashMap<>();
        boolean tickInProgress = false;
    }

    private static final Map<ModularRouterBlockEntity, RouterState> STATES = new WeakHashMap<>();

    private BeamContinuityRegistry() {
    }

    private static RouterState stateOf(ModularRouterBlockEntity router) {
        return STATES.computeIfAbsent(router, r -> new RouterState());
    }

    public static void beginTick(ModularRouterBlockEntity router) {
        RouterState state = stateOf(router);
        state.pendingThisTick.clear();
        state.tickInProgress = true;
    }

    public static void reportActive(ModularRouterBlockEntity router, Object beamKey, Runnable startAction, Runnable stopAction) {
        RouterState state = stateOf(router);
        if (!state.tickInProgress) {
            startAction.run();
            state.confirmedActive.put(beamKey, stopAction);
            return;
        }
        state.pendingThisTick.put(beamKey, stopAction);
        if (!state.confirmedActive.containsKey(beamKey)) {
            startAction.run();
        }
    }

    public static void endTick(ModularRouterBlockEntity router) {
        RouterState state = stateOf(router);
        for (Map.Entry<Object, Runnable> entry : state.confirmedActive.entrySet()) {
            if (!state.pendingThisTick.containsKey(entry.getKey())) {
                entry.getValue().run();
            }
        }
        state.confirmedActive.clear();
        state.confirmedActive.putAll(state.pendingThisTick);
        state.tickInProgress = false;
    }
}
