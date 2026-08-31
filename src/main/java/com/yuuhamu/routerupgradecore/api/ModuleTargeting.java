package com.yuuhamu.routerupgradecore.api;

import com.yuuhamu.routerupgradecore.mixin.CompiledModuleAccessor;
import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;

import java.util.List;

public final class ModuleTargeting {

    private ModuleTargeting() {
    }

    public static ModuleTarget getTarget(CompiledModule compiled) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetTarget();
    }

    public static List<ModuleTarget> getTargets(CompiledModule compiled) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetTargets();
    }

    public static Direction getFacing(CompiledModule compiled) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetFacing();
    }

    public static int getRange(CompiledModule compiled) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetRange();
    }

    public static int getRangeSquared(CompiledModule compiled) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetRangeSquared();
    }

    public static int getAugmentCount(CompiledModule compiled, Item augment) {
        return ((CompiledModuleAccessor) compiled).routerupgradecore$invokeGetAugmentCount(augment);
    }
}

