package com.yuuhamu.routerupgradecore.api;

import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;

import java.util.List;

/**
 * NeoForge版ではCompiledModuleのgetTarget/getTargets/getRange/getRangeSquared/getAugmentCount/
 * getAbsoluteFacing(旧getFacing)がすべてpublicへ変更されたため、Mixin Accessor経由の呼び出しは
 * 不要になり、直接呼び出しに単純化した。
 */
public final class ModuleTargeting {

    private ModuleTargeting() {
    }

    public static ModuleTarget getTarget(CompiledModule compiled) {
        return compiled.getTarget();
    }

    public static List<ModuleTarget> getTargets(CompiledModule compiled) {
        return compiled.getTargets();
    }

    public static Direction getFacing(CompiledModule compiled) {
        return compiled.getAbsoluteFacing();
    }

    public static int getRange(CompiledModule compiled) {
        return compiled.getRange();
    }

    public static int getRangeSquared(CompiledModule compiled) {
        return compiled.getRangeSquared();
    }

    public static int getAugmentCount(CompiledModule compiled, Item augment) {
        return compiled.getAugmentCount(augment);
    }
}
