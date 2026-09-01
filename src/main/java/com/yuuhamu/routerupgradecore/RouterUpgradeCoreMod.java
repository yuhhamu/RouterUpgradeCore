package com.yuuhamu.routerupgradecore;

import com.yuuhamu.routerupgradecore.network.PacketHandler;
import com.yuuhamu.routerupgradecore.registry.ModCreativeTabs;
import com.yuuhamu.routerupgradecore.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RouterUpgradeCoreMod.MODID)
public class RouterUpgradeCoreMod {

    public static final String MODID = "routerupgradecore";

    public RouterUpgradeCoreMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.REGISTRY.register(modEventBus);
        ModCreativeTabs.REGISTRY.register(modEventBus);

        PacketHandler.register();
    }
}

