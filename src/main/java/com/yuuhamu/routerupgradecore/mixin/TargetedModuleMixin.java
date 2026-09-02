package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.item.module.ITargetedModule;
import me.desht.modularrouters.item.module.adapter.TargetedModuleAdapter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * NeoForge版ではTargetedModuleが具象クラスからITargetedModuleインターフェース(defaultメソッド)へ
 * 変更されたため、Mixinのインターフェース注入(@Inject)は使用不可("Injector in interface is
 * unsupported")。実際にisValidTarget()相当の判定を行っているのは
 * {@code ITargetedModule.canSelectTarget(UseOnContext)}(インターフェースのstaticメソッド、
 * 内部でisValidTarget()を呼びAddModuleTargetEventを発火する)であり、その呼び出し元である
 * {@code TargetedModuleAdapter}(Recordクラス、具象)側の呼び出し箇所を@Redirectで差し替える。
 */
@Mixin(value = TargetedModuleAdapter.class, remap = false)
public abstract class TargetedModuleMixin {

    @Redirect(method = {"useOn", "handleMultiTarget"}, at = @At(value = "INVOKE",
            target = "Lme/desht/modularrouters/item/module/ITargetedModule;canSelectTarget(Lnet/minecraft/world/item/context/UseOnContext;)Z"))
    private static boolean routerupgradecore$canSelectTarget(UseOnContext ctx) {
        Item moduleItem = ctx.getItemInHand().getItem();
        if (ModeRegistry.shouldRelaxTarget(moduleItem, ctx)) {
            return !ctx.getLevel().isEmptyBlock(ctx.getClickedPos());
        }
        return ITargetedModule.canSelectTarget(ctx);
    }
}
