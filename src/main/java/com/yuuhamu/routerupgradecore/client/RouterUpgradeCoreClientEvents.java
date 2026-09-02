package com.yuuhamu.routerupgradecore.client;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import com.yuuhamu.routerupgradecore.api.RouterVisualBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RouterUpgradeCoreMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RouterUpgradeCoreClientEvents {

    private RouterUpgradeCoreClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        List<Block> targets = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
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
            for (Block block : BuiltInRegistries.BLOCK) {
                if (block instanceof RouterVisualBlock) {
                    ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
                }
            }
        });
    }
}
