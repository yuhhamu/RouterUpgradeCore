package com.yuuhamu.routerupgradecore.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import com.yuuhamu.routerupgradecore.network.BufferSlotInteractMessage;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.gui.ModularRouterScreen;
import me.desht.modularrouters.container.RouterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ModularRouterScreen.class, remap = false)
public abstract class ModularRouterScreenMixin {

    private static final int ICON_X = 30;
    private static final int ICON_Y = 39;

    private static final int BUFFER_X = 8;
    private static final int BUFFER_Y = 40;

    private static final int BUFFER_DISPLAY_WIDTH = 16;
    private static final int BUFFER_DISPLAY_HEIGHT = 16;
    private static final int BUFFER_DISPLAY_BG_COLOR = 0xFF373737;

    @Inject(method = "renderLabels", at = @At("TAIL"))
    private void routerupgradecore$renderActiveModeIcon(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        ModularRouterBlockEntity router = ((RouterMenu) ((MenuAccess<?>) this).getMenu()).getRouter();
        Item marker = ModeRegistry.getActiveMarkerItem(router);
        if (marker == null) {
            return;
        }
        ItemStack stack = new ItemStack(marker);
        graphics.renderItem(stack, ICON_X, ICON_Y);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, ICON_X, ICON_Y);

        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        renderBufferGauge(graphics, router, provider, mouseX, mouseY);
    }

    private void renderBufferGauge(GuiGraphics graphics, ModularRouterBlockEntity router,
                                    RouterModeProvider provider, int mouseX, int mouseY) {
        graphics.fill(BUFFER_X, BUFFER_Y, BUFFER_X + BUFFER_DISPLAY_WIDTH, BUFFER_Y + BUFFER_DISPLAY_HEIGHT,
                BUFFER_DISPLAY_BG_COLOR);

        ResourceLocation texture = provider.getBufferContentTexture(router);
        if (texture != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance()
                    .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                    .apply(texture);
            int tint = provider.getBufferContentTintColor(router);
            float red = ((tint >> 16) & 0xFF) / 255f;
            float green = ((tint >> 8) & 0xFF) / 255f;
            float blue = (tint & 0xFF) / 255f;
            RenderSystem.setShaderColor(red, green, blue, 1f);
            graphics.blit(BUFFER_X, BUFFER_Y, 0, BUFFER_DISPLAY_WIDTH, BUFFER_DISPLAY_HEIGHT, sprite);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }

        int localMouseX = mouseX - ((ModularRouterScreen) (Object) this).getGuiLeft();
        int localMouseY = mouseY - ((ModularRouterScreen) (Object) this).getGuiTop();

        if (localMouseX >= BUFFER_X && localMouseX < BUFFER_X + BUFFER_DISPLAY_WIDTH
                && localMouseY >= BUFFER_Y && localMouseY < BUFFER_Y + BUFFER_DISPLAY_HEIGHT) {
            List<Component> tooltip = provider.getBufferTooltip(router);
            if (!tooltip.isEmpty()) {
                graphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, localMouseX, localMouseY);
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void routerupgradecore$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0 && button != 1) {
            return;
        }
        ModularRouterBlockEntity router = ((RouterMenu) ((MenuAccess<?>) this).getMenu()).getRouter();
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        int localMouseX = (int) mouseX - ((ModularRouterScreen) (Object) this).getGuiLeft();
        int localMouseY = (int) mouseY - ((ModularRouterScreen) (Object) this).getGuiTop();
        if (localMouseX < BUFFER_X || localMouseX >= BUFFER_X + BUFFER_DISPLAY_WIDTH
                || localMouseY < BUFFER_Y || localMouseY >= BUFFER_Y + BUFFER_DISPLAY_HEIGHT) {
            return;
        }
        PacketDistributor.sendToServer(new BufferSlotInteractMessage(router.getBlockPos(), button == 0));
        cir.setReturnValue(true);
    }
}
