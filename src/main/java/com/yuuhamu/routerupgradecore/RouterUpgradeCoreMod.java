package com.yuuhamu.routerupgradecore;

import com.yuuhamu.routerupgradecore.network.PacketHandler;
import net.minecraftforge.fml.common.Mod;

@Mod(RouterUpgradeCoreMod.MODID)
public class RouterUpgradeCoreMod {

    public static final String MODID = "routerupgradecore";

    public RouterUpgradeCoreMod() {
        PacketHandler.register();
    }
}

