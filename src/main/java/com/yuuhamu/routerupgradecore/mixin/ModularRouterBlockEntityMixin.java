package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.BeamDataAccess;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.CamouflageableBlock;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.util.BeamData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
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
    private BlockState camouflage;

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

    @Inject(method = "processClientSync", at = @At("TAIL"))
    private void routerupgradecore$processClientSync(CompoundTag tag, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;

        Item syncedMarker = null;
        if (tag.contains(ModeRegistry.CLIENT_SYNC_MARKER_NBT_KEY)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString(ModeRegistry.CLIENT_SYNC_MARKER_NBT_KEY));
            if (id != null) {
                syncedMarker = ForgeRegistries.ITEMS.getValue(id);
            }
        }
        ModeRegistry.recordClientSyncedMarker(router, syncedMarker);

        Level level = this.nonNullLevel();
        if (level.isClientSide) {
            router.requestModelDataUpdate();
            level.setBlocksDirty(router.getBlockPos(), Blocks.AIR.defaultBlockState(), router.getBlockState());
        }

        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.handleUpdateTag(router, tag);
        }
    }

    @Inject(method = "compileUpgrades", at = @At("TAIL"))
    private void routerupgradecore$compileUpgrades(CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.onCompileUpgrades(router);
        }
        router.requestModelDataUpdate();
        Level level = this.nonNullLevel();
        if (!level.isClientSide) {
            BlockState state = router.getBlockState();
            level.sendBlockUpdated(router.getBlockPos(), state, state, 3);
        }
    }

    @Inject(method = "getModelData", at = @At("RETURN"), cancellable = true)
    private void routerupgradecore$getModelData(CallbackInfoReturnable<ModelData> cir) {
        if (this.camouflage != null) {
            return;
        }
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        BlockState visual = provider.getVisualCamouflage(router);
        if (visual == null) {
            return;
        }
        cir.setReturnValue(ModelData.builder().with(CamouflageableBlock.CAMOUFLAGE_STATE, visual).build());
        router.requestModelDataUpdate();
    }

    @Inject(method = "getCamouflage", at = @At("RETURN"), cancellable = true)
    private void routerupgradecore$getCamouflage(CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null) {
            return;
        }
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        BlockState visual = provider.getVisualCamouflage(router);
        if (visual == null) {
            return;
        }
        cir.setReturnValue(visual);
    }

    @Inject(method = "addItemBeam", at = @At("HEAD"), cancellable = true)
    private void routerupgradecore$addItemBeam(BeamData beamData, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        Integer imageColor = ModeRegistry.getActiveImageColor(router);
        if (imageColor == null) {
            return;
        }
        BeamData recolored = BeamDataAccess.withColor(beamData, imageColor);
        if (this.nonNullLevel().isClientSide) {
            this.beams.add(recolored);
            this.cachedRenderAABB = null;
        } else {
            this.pendingBeams.add(recolored);
        }
        ci.cancel();
    }
}

