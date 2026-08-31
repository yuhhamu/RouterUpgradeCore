package com.yuuhamu.routerupgradecore.client;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import com.yuuhamu.routerupgradecore.api.RouterVisualBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = RouterUpgradeCoreMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RouterUpgradeCoreClientEvents {

    private RouterUpgradeCoreClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        List<Block> targets = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof RouterVisualBlock) {
                targets.add(block);
            }
        }
        if (targets.isEmpty()) {
            return;
        }
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return 0xFFFFFF;
            }
            if (state.getBlock() instanceof RouterVisualBlock visual) {
                return visual.getRouterImageColor();
            }
            return 0xFFFFFF;
        }, targets.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Block block : ForgeRegistries.BLOCKS) {
                if (block instanceof RouterVisualBlock) {
                    ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
                }
            }
        });
    }
}

