package com.yuuhamu.routerupgradecore.network;

import com.yuuhamu.routerupgradecore.RouterUpgradeCoreMod;
import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.container.RouterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BufferSlotInteractMessage(BlockPos pos, boolean extract) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BufferSlotInteractMessage> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RouterUpgradeCoreMod.MODID, "buffer_slot_interact"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BufferSlotInteractMessage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BufferSlotInteractMessage::pos,
            ByteBufCodecs.BOOL, BufferSlotInteractMessage::extract,
            BufferSlotInteractMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BufferSlotInteractMessage payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (!(player.containerMenu instanceof RouterMenu menu)) return;
            ModularRouterBlockEntity router = menu.getRouter();
            if (router.isRemoved() || !router.getBlockPos().equals(payload.pos())) return;
            RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
            if (provider == null) return;
            if (payload.extract()) {
                provider.onBufferSlotExtract(router, player);
            } else {
                provider.onBufferSlotCollect(router, player);
            }
        });
    }
}
