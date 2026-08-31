package com.yuuhamu.routerupgradecore.internal;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 「稼働タイミング(executeModulesの1回の呼び出し)ごとに輸送が継続しているか」を
 * 追跡し、継続中は視覚効果(ビーム等)を再生成せず表示したままにし、輸送が行われ
 * なかった稼働タイミングがあれば即座に終了させるための汎用レジストリ。
 *
 * Fluid/Chemical等の具体的な種別には一切関知しない。RouterModeProviderの実装が
 * 稼働タイミングごとに「このキーで輸送に成功した」ことをreportActive()で報告し、
 * このレジストリが「新規開始」か「継続」かを判定してstartAction/stopActionを
 * 適切なタイミングで1回だけ呼び出す。beginTick()/endTick()はexecuteModules()の
 * 開始・終了に合わせてMixin側から呼び出される(ModuleExecutionMixin参照)。
 */
public final class BeamContinuityRegistry {

    private static final class RouterState {
        // 直前に完了した稼働タイミングまでに確認された、アクティブなキーとその終了時アクション
        final Map<Object, Runnable> confirmedActive = new HashMap<>();
        // 現在処理中の稼働タイミングで報告されたキーとその終了時アクション
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
            // beginTick()が呼ばれていない状態(想定外の呼び出し順)でも、
            // 単発の開始としてフォールバックする。
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
