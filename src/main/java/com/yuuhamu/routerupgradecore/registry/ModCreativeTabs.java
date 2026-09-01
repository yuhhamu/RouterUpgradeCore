package com.yuuhamu.routerupgradecore.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/*
 * 1.19.2向け移植メモ(2026-09-01): 1.19.2にはRegistries.CREATIVE_MODE_TABによるレジストリ登録が存在せず、
 * CreativeModeTabは直接インスタンス化してCreativeModeTab.TABS配列へ自己登録する旧方式(コンストラクタが
 * 副作用でTABS配列に自身を格納する)。displayItems()ビルダーも存在しないため、タブへのアイテム表示は
 * ModItems側のItem.Properties#tab(...)呼び出しで行う(ModItems.javaも合わせて修正)。
 */
public class ModCreativeTabs {

    public static final CreativeModeTab ROUTER_UPGRADE_CORE_TAB = new CreativeModeTab(CreativeModeTab.TABS.length, "routerupgradecore") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MODE_UPGRADE_CORE.get());
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("itemGroup.routerupgradecore");
        }
    };
}
