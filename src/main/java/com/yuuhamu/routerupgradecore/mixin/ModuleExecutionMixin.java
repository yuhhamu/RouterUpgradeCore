package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.ModuleKind;
import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.BeamContinuityRegistry;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.item.module.ModuleItem;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(value = ModularRouterBlockEntity.class, remap = false)
public abstract class ModuleExecutionMixin {

    @Inject(method = "executeModules", at = @At("HEAD"))
    private void routerupgradecore$beginBeamContinuityTick(boolean pulseOnly, CallbackInfo ci) {
        BeamContinuityRegistry.beginTick((ModularRouterBlockEntity) (Object) this);
    }

    @Inject(method = "executeModules", at = @At("TAIL"))
    private void routerupgradecore$endBeamContinuityTick(boolean pulseOnly, CallbackInfo ci) {
        BeamContinuityRegistry.endTick((ModularRouterBlockEntity) (Object) this);
    }

    private static final Field MODULE_FIELD;

    static {
        try {
            MODULE_FIELD = CompiledModule.class.getDeclaredField("module");
            MODULE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("ModularRoutersのCompiledModule.moduleフィールドが見つかりません(本家APIが変更された可能性があります)", e);
        }
    }

    @Redirect(method = "executeModules", at = @At(value = "INVOKE",
            target = "Lme/desht/modularrouters/logic/compiled/CompiledModule;execute(Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;)Z"))
    private boolean routerupgradecore$redirectExecute(CompiledModule compiledModule, ModularRouterBlockEntity router) {
        ModuleItem moduleItem = getModuleItem(compiledModule);
        ModuleKind kind = moduleItem == null ? null : resolveKind(moduleItem);
        if (kind != null) {
            RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
            if (provider != null) {
                return provider.executeModuleLogic(router, kind, compiledModule);
            }
        }
        return compiledModule.execute(router);
    }

    private static ModuleItem getModuleItem(CompiledModule compiled) {
        try {
            return (ModuleItem) MODULE_FIELD.get(compiled);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("CompiledModule.moduleフィールドの読み取りに失敗しました", e);
        }
    }

    private static ModuleKind resolveKind(ModuleItem moduleItem) {
        if (moduleItem == ModItems.PULLER_MODULE_1.get() || moduleItem == ModItems.PULLER_MODULE_2.get()) {
            return ModuleKind.PULLER;
        }
        if (moduleItem == ModItems.SENDER_MODULE_1.get() || moduleItem == ModItems.SENDER_MODULE_2.get()
                || moduleItem == ModItems.SENDER_MODULE_3.get()) {
            return ModuleKind.SENDER;
        }
        if (moduleItem == ModItems.DISTRIBUTOR_MODULE.get()) {
            return ModuleKind.DISTRIBUTOR;
        }
        if (moduleItem == ModItems.VOID_MODULE.get()) {
            return ModuleKind.VOID;
        }
        return null;
    }
}

