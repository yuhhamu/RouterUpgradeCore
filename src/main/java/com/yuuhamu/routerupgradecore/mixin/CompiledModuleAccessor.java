package com.yuuhamu.routerupgradecore.mixin;

/**
 * 廃止: NeoForge版のme.desht.modularrouters.logic.compiled.CompiledModuleでは
 * getTarget/getTargets/getRange/getRangeSquared/getAugmentCount/getAbsoluteFacing(旧getFacing)が
 * すべてpublicへ変更されたため、Mixin Accessor経由でのアクセスは不要になった。
 * 呼び出し側は com.yuuhamu.routerupgradecore.api.ModuleTargeting から直接呼び出す。
 * (このファイルはdevice_bashのマウント制約でファイル削除ができないため、
 * 空のプレースホルダとして残置している。routerupgradecore.mixins.jsonからは既に除外済み。)
 */
final class CompiledModuleAccessor {
    private CompiledModuleAccessor() {
    }
}
