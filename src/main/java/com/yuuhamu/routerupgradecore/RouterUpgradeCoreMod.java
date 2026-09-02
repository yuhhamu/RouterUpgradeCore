package com.yuuhamu.routerupgradecore;

import com.yuuhamu.routerupgradecore.network.PacketHandler;
import com.yuuhamu.routerupgradecore.registry.ModCreativeTabs;
import com.yuuhamu.routerupgradecore.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(RouterUpgradeCoreMod.MODID)
public class RouterUpgradeCoreMod {

    public static final String MODID = "routerupgradecore";

    public RouterUpgradeCoreMod(IEventBus modEventBus, ModContainer container) {
        ModItems.REGISTRY.register(modEventBus);
        ModCreativeTabs.REGISTRY.register(modEventBus);

        PacketHandler.register(modEventBus);
    }
}
