package com.yuuhamu.routerupgradecore.registry;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/*
 * 1.19.2向け移植メモ(2026-09-01): 1.19.2にはRegistries.CREATIVE_MODE_TABによるレジストリ登録が存在せず、
 * CreativeModeTabは直接インスタンス化してCreativeModeTab.TABS配列へ自己登録する旧方式(コンストラクタが
 * 副作用でTABS配列に自身を格納する)。displayItems()ビルダーも存在しないため、タブへのアイテム表示は
 * fillItemList(NonNullList)のオーバーライドで行う。
 *
 * 追記(2026-09-01): 当初はModItems側のItem.Properties#tab(...)呼び出しでタブ紐付けを行っていたが、
 * このJarを外部Mod依存として読み込む別プロジェクト(FluidRouterUpgrade)のdev-run環境で
 * NoSuchMethodError(m_41491_)が発生することが判明した(自プロジェクト単体のdev-runでは再現しない、
 * reobf後のJarを外部依存として読み込んだ場合のみ発生する問題)。FluidRouterUpgrade側で既に確立している
 * 回避策(fillItemListオーバーライドでタブへ手動追加)と統一し、Item.Properties#tab(...)は使わない。
 */
public class ModCreativeTabs {

    public static final CreativeModeTab ROUTER_UPGRADE_CORE_TAB = new CreativeModeTab("routerupgradecore") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MODE_UPGRADE_CORE.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            items.add(new ItemStack(ModItems.MODE_UPGRADE_CORE.get()));
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("itemGroup.routerupgradecore");
        }
    };
}
