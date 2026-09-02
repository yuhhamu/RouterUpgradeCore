package com.yuuhamu.routerupgradecore.api;

import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.logic.compiled.CompiledModule;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.List;

/**
 * RouterUpgradeCoreが提供する「モード拡張」の実装インターフェース。
 * <p>
 * NeoForge版では、Capability APIが{@code LazyOptional}/{@code Capability<T>}方式から
 * {@code BlockCapability<T,C>}方式へ刷新されたことに伴い、{@link #getCapability}は
 * 汎用的なジェネリクスメソッドとして再設計されている。ModularRouters本体が既に
 * BLOCK単位で登録済みのcapability型(ItemHandler・EnergyStorage等)とは重複登録できないため、
 * 本メソッドは主に本体が公開していないcapability型(FluidHandler・Chemical等)を
 * アドオンMod自身の{@code RegisterCapabilitiesEvent}経由で公開する用途を想定している。
 * アドオンMod側は自身の{@code RegisterCapabilitiesEvent}リスナーで
 * {@code RouterUpgradeCore.getActiveCapability(router, capability, side)}を呼び出し、
 * 本メソッドへ委譲すること。
 */
public interface RouterModeProvider {

    <T> T getCapability(ModularRouterBlockEntity router, BlockCapability<T, Direction> capability,
                         Direction side);

    boolean relaxTargetValidation(Item moduleItem, UseOnContext context);

    boolean executeModuleLogic(ModularRouterBlockEntity router, ModuleKind kind, CompiledModule vanillaCompiledModule);

    void load(ModularRouterBlockEntity router, CompoundTag tag, HolderLookup.Provider registries);

    void saveAdditional(ModularRouterBlockEntity router, CompoundTag tag, HolderLookup.Provider registries);

    void getUpdateTag(ModularRouterBlockEntity router, CompoundTag tag, HolderLookup.Provider registries);

    void handleUpdateTag(ModularRouterBlockEntity router, CompoundTag tag, HolderLookup.Provider registries);

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
