package com.yuuhamu.routerupgradecore.registry;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RouterUpgradeCoreMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ROUTER_UPGRADE_CORE_TAB = REGISTRY.register("router_upgrade_core_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.routerupgradecore"))
                    .icon(() -> new ItemStack(ModItems.MODE_UPGRADE_CORE.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.MODE_UPGRADE_CORE.get());
                    })
                    .build());
}
