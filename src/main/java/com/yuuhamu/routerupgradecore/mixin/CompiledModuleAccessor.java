package com.yuuhamu.routerupgradecore.mixin;

import me.desht.modularrouters.logic.ModuleTarget;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = CompiledModule.class, remap = false)
public interface CompiledModuleAccessor {

    @Invoker("getTarget")
    ModuleTarget routerupgradecore$invokeGetTarget();

    @Invoker("getTargets")
    List<ModuleTarget> routerupgradecore$invokeGetTargets();

    @Invoker("getFacing")
    Direction routerupgradecore$invokeGetFacing();

    @Invoker("getRange")
    int routerupgradecore$invokeGetRange();

    @Invoker("getRangeSquared")
    int routerupgradecore$invokeGetRangeSquared();

    @Invoker("getAugmentCount")
    int routerupgradecore$invokeGetAugmentCount(Item augment);
}

