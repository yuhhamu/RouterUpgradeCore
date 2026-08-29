package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.BeamDataAccess;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.CamouflageableBlock;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.util.BeamData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ModularRouterBlockEntity.class, remap = false)
public abstract class ModularRouterBlockEntityMixin {

    @Shadow
    @Final
    public List<BeamData> beams;

    @Shadow
    @Final
    public List<BeamData> pendingBeams;

    @Shadow
    private AABB cachedRenderAABB;

    @Shadow
    public abstract Level nonNullLevel();

    @Inject(method = "getCapability", at = @At("RETURN"), cancellable = true)
    private void routerupgradecore$getCapability(Capability<?> capability, Direction side,
                                                  CallbackInfoReturnable<LazyOptional<?>> cir) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            cir.setReturnValue(provider.getCapability(router, capability, side, cir.getReturnValue()));
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void routerupgradecore$load(CompoundTag tag, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.load(router, tag);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void routerupgradecore$saveAdditional(CompoundTag tag, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.saveAdditional(router, tag);
        }
    }

    @Inject(method = "getUpdateTag", at = @At("RETURN"))
    private void routerupgradecore$getUpdateTag(CallbackInfoReturnable<CompoundTag> cir) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.getUpdateTag(router, cir.getReturnValue());
        }
    }

    @Inject(method = "handleUpdateTag", at = @At("TAIL"))
    private void routerupgradecore$handleUpdateTag(CompoundTag tag, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.handleUpdateTag(router, tag);
        }
    }

    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void routerupgradecore$setRemoved(CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.onRemoved(router);
        }
    }

    @Inject(method = "compileUpgrades", at = @At("TAIL"))
    private void routerupgradecore$compileUpgrades(CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.onCompileUpgrades(router);
        }
    }

    @Inject(method = "getModelData", at = @At("RETURN"), cancellable = true)
    private void routerupgradecore$getModelData(CallbackInfoReturnable<ModelData> cir) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        if (router.getCamouflage() != null) {
            return;
        }
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        BlockState visual = provider.getVisualCamouflage(router);
        if (visual == null) {
            return;
        }
        cir.setReturnValue(ModelData.builder().with(CamouflageableBlock.CAMOUFLAGE_STATE, visual).build());
    }

    @Inject(method = "addItemBeam", at = @At("HEAD"), cancellable = true)
    private void routerupgradecore$addItemBeam(BeamData beamData, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        Integer overrideColor = provider.getBeamColor(router);
        if (overrideColor == null) {
            return;
        }
        BeamData recolored = BeamDataAccess.withColor(beamData, overrideColor);
        if (this.nonNullLevel().isClientSide) {
            this.beams.add(recolored);
            this.cachedRenderAABB = null;
        } else {
            this.pendingBeams.add(recolored);
        }
        ci.cancel();
    }
}
