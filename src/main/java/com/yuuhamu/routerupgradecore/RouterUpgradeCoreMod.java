package com.yuuhamu.routerupgradecore;

import com.yuuhamu.routerupgradecore.network.PacketHandler;
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
        // 1.19.2向け移植メモ(2026-09-01): ModCreativeTabsはレジストリ登録を持たない(CreativeModeTabの
        // コンストラクタが自己登録するため、ここでのregister呼び出しは不要かつ不可能)。

        PacketHandler.register();
    }
}
