package com.yuuhamu.routerupgradecore.api;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public interface RouterModeProvider {

    LazyOptional<?> getCapability(ModularRouterBlockEntity router, Capability<?> capability,
                                   Direction side, LazyOptional<?> vanillaDefault);

    boolean relaxTargetValidation(Item moduleItem, UseOnContext context);

    boolean executeModuleLogic(ModularRouterBlockEntity router, ModuleKind kind, CompiledModule vanillaCompiledModule);

    void load(ModularRouterBlockEntity router, CompoundTag tag);

    void saveAdditional(ModularRouterBlockEntity router, CompoundTag tag);

    void getUpdateTag(ModularRouterBlockEntity router, CompoundTag tag);

    void handleUpdateTag(ModularRouterBlockEntity router, CompoundTag tag);

    void onRemoved(ModularRouterBlockEntity router);

    void onCompileUpgrades(ModularRouterBlockEntity router);

    default BlockState getVisualCamouflage(ModularRouterBlockEntity router) {
        return null;
    }

    default ResourceLocation getGuiTexture() {
        return null;
    }

    default ResourceLocation getBufferContentTexture(ModularRouterBlockEntity router) {
        return null;
    }

    default int getBufferContentTintColor(ModularRouterBlockEntity router) {
        return 0xFFFFFF;
    }

    default List<Component> getBufferTooltip(ModularRouterBlockEntity router) {
        return List.of();
    }

    default boolean onBufferSlotExtract(ModularRouterBlockEntity router, Player player) {
        return false;
    }

    default boolean onBufferSlotCollect(ModularRouterBlockEntity router, Player player) {
        return false;
    }

}

