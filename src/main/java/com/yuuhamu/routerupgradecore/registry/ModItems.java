package com.yuuhamu.routerupgradecore.registry;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, RouterUpgradeCoreMod.MODID);

    // 1.19.2向け移植メモ(2026-09-01追記): Item.Properties#tab(CreativeModeTab)(SRG: m_41491_)を
    // 呼び出すと、このJarを外部Mod依存として読み込む別プロジェクト(FluidRouterUpgrade)のdev-run環境で
    // NoSuchMethodErrorが発生することが判明した(自プロジェクト単体のdev-runでは問題なく動作する
    // ため気づきにくい)。原因は、reobf後のこのJarはSRG名でm_41491_を呼び出すが、外部Modとして
    // 読み込まれたdev-run環境側のランタイム remap 層がこの呼び出しを解決できないため。
    // FluidRouterUpgrade側で既に確立している回避策(ModCreativeTabs#fillItemList(NonNullList)を
    // オーバーライドしてタブへ手動でアイテムを追加する方式)と統一し、ここでは.tab(...)を呼ばない。
    public static final RegistryObject<Item> MODE_UPGRADE_CORE = REGISTRY.register("mode_upgrade_core",
            () -> new Item(new Item.Properties()));
}
