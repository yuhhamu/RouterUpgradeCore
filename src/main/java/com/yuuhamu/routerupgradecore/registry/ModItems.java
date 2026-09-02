package com.yuuhamu.routerupgradecore.registry;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.ITEM, RouterUpgradeCoreMod.MODID);

    public static final DeferredHolder<Item, Item> MODE_UPGRADE_CORE = REGISTRY.register("mode_upgrade_core",
            () -> new Item(new Item.Properties()));
}
