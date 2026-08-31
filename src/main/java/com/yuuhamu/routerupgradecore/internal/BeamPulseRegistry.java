package com.yuuhamu.routerupgradecore.internal;

import me.desht.modularrouters.util.BeamData;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * クライアント側でのみ使用する、個々のBeamDataインスタンスに対して
 * 「Vanilla本体の1秒周期アルファ点滅を適用しない(常に一定の明るさで描画する)」
 * フラグを立てるためのレジストリ。
 *
 * Vanilla本体のModularRouterBERはビームの種別を区別せず、全てのBeamDataに同じ
 * 点滅アルファ計算(renderBeamLineの中心太線部分)を適用する。RouterUpgradeCore
 * 経由の各モード(FluidRouterUpgrade等)が生成する中心ビームだけを個別に区別する
 * ため、BeamDataの参照そのものをキーにしたWeakHashMapで管理する。Vanilla側の
 * beams/pendingBeamsリストからビームが失効・GCされれば自動的にエントリも消える。
 */
public final class BeamPulseRegistry {

    private static final Set<BeamData> NO_PULSE = Collections.newSetFromMap(new WeakHashMap<>());

    private BeamPulseRegistry() {
    }

    public static void markNoPulse(BeamData beam) {
        NO_PULSE.add(beam);
    }

    public static boolean isNoPulse(BeamData beam) {
        return NO_PULSE.contains(beam);
    }
}
