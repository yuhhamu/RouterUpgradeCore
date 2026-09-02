package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModularRouterBlockEntity.class, remap = false)
public abstract class ModularRouterBlockEntityVanillaOverrideMixin {

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void routerupgradecore$loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.load(router, tag, registries);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void routerupgradecore$saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.saveAdditional(router, tag, registries);
        }
    }

    @Inject(method = "getUpdateTag", at = @At("RETURN"))
    private void routerupgradecore$getUpdateTag(HolderLookup.Provider registries, CallbackInfoReturnable<CompoundTag> cir) {
        ModularRouterBlockEntity router = (ModularRouterBlockEntity) (Object) this;
        CompoundTag tag = cir.getReturnValue();

        Item marker = ModeRegistry.getActiveMarkerItem(router);
        if (marker != null) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(marker);
            if (id != null) {
                tag.putString(ModeRegistry.CLIENT_SYNC_MARKER_NBT_KEY, id.toString());
            }
        } else {
            tag.remove(ModeRegistry.CLIENT_SYNC_MARKER_NBT_KEY);
        }

        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider != null) {
            provider.getUpdateTag(router, tag, registries);
        }
    }
}
