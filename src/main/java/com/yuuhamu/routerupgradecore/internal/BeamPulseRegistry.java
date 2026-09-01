package com.yuuhamu.routerupgradecore.internal;

import me.desht.modularrouters.util.BeamData;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

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
