package com.yuuhamu.routerupgradecore.registry;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, RouterUpgradeCoreMod.MODID);

    public static final RegistryObject<Item> MODE_UPGRADE_CORE = REGISTRY.register("mode_upgrade_core",
            () -> new Item(new Item.Properties()));
}
