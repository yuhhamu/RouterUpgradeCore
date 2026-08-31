package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.internal.BeamPulseRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.render.blockentity.ModularRouterBER;
import me.desht.modularrouters.util.BeamData;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModularRouterBER.class, remap = false)
public abstract class ModularRouterBERMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;getCamouflage()Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState routerupgradecore$realCamouflageForHighlightOnly(ModularRouterBlockEntity router) {
        return ((ModularRouterCamouflageAccessor) router).routerupgradecore$getRealCamouflage();
    }

    // renderBeamLine内で計算される中心太線(BEAM_LINE_THICK)用のアルファ値は、
    // Vanilla本体の実装により常にgetGameTime()を基にした1秒周期のsin波
    // (alpha 32〜160)で点滅する。RouterUpgradeCore経由のモード(FluidRouterUpgrade等)が
    // BeamPulseRegistry.markNoPulse()で登録した中心ビームに限り、この点滅を無効化して
    // 常に最大値(160、最も明るい状態)で一定描画する。点滅演出そのものは各実装側の
    // 周辺エフェクト(例: FluidRouterUpgradeのハローライン)側に持たせる方針。
    @ModifyVariable(method = "renderBeamLine", at = @At("STORE"), ordinal = 0)
    private int routerupgradecore$fixAlphaForNoPulseBeam(int alpha, BeamData beam) {
        return BeamPulseRegistry.isNoPulse(beam) ? 160 : alpha;
    }
}
