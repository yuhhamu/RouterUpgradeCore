package com.yuuhamu.routerupgradecore.network;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.container.RouterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BufferSlotInteractMessage {

    private final BlockPos pos;
    private final boolean extract;

    public BufferSlotInteractMessage(BlockPos pos, boolean extract) {
        this.pos = pos;
        this.extract = extract;
    }

    BufferSlotInteractMessage(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        extract = buffer.readBoolean();
    }

    public void toBytes(FriendlyByteBuf byteBuf) {
        byteBuf.writeBlockPos(pos);
        byteBuf.writeBoolean(extract);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            if (!(player.containerMenu instanceof RouterMenu menu)) {
                return;
            }
            ModularRouterBlockEntity router = menu.getRouter();
            if (router.isRemoved() || !router.getBlockPos().equals(pos)) {
                return;
            }
            RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
            if (provider == null) {
                return;
            }
            if (extract) {
                provider.onBufferSlotExtract(router, player);
            } else {
                provider.onBufferSlotCollect(router, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

