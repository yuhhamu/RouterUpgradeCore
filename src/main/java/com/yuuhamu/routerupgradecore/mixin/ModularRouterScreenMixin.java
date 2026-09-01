package com.yuuhamu.routerupgradecore.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import com.yuuhamu.routerupgradecore.network.BufferSlotInteractMessage;
import com.yuuhamu.routerupgradecore.network.PacketHandler;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.gui.ModularRouterScreen;
import me.desht.modularrouters.container.RouterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/*
 * 1.19.2向け移植メモ(2026-09-01): 1.19.2にはGuiGraphicsが存在せず、renderLabels/描画はPoseStack直描画。
 * Screenの protected な renderComponentTooltip(PoseStack, List<Component>, int, int) を呼ぶため、
 * BufferHandlerMixin(ItemStackHandlerを継承)と同じ確立済みパターンで、このMixinクラス自体を
 * AbstractContainerScreen<RouterMenu>(実際の継承チェーンの一部)に見せかけて継承する。
 * コンストラクタはMixinのマージ対象外(呼び出されない)ため、javacの型解決だけを満たすダミー実装。
 */
@Mixin(value = ModularRouterScreen.class, remap = false)
public abstract class ModularRouterScreenMixin extends AbstractContainerScreen<RouterMenu> {

    private static final int ICON_X = 30;
    private static final int ICON_Y = 39;

    private static final int BUFFER_X = 8;
    private static final int BUFFER_Y = 40;

    private static final int BUFFER_DISPLAY_WIDTH = 16;
    private static final int BUFFER_DISPLAY_HEIGHT = 16;
    private static final int BUFFER_DISPLAY_BG_COLOR = 0xFF373737;

    protected ModularRouterScreenMixin(RouterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = {"renderLabels", "m_7027_"}, at = @At("TAIL"))
    private void routerupgradecore$renderActiveModeIcon(PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        ModularRouterBlockEntity router = ((RouterMenu) ((MenuAccess<?>) this).getMenu()).getRouter();
        Item marker = ModeRegistry.getActiveMarkerItem(router);
        if (marker == null) {
            return;
        }
        ItemStack stack = new ItemStack(marker);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, ICON_X, ICON_Y);
        Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(
                Minecraft.getInstance().font, stack, ICON_X, ICON_Y);

        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        if (provider == null) {
            return;
        }
        renderBufferGauge(poseStack, router, provider, mouseX, mouseY);
    }

    private void renderBufferGauge(PoseStack poseStack, ModularRouterBlockEntity router,
                                    RouterModeProvider provider, int mouseX, int mouseY) {
        GuiComponent.fill(poseStack, BUFFER_X, BUFFER_Y, BUFFER_X + BUFFER_DISPLAY_WIDTH, BUFFER_Y + BUFFER_DISPLAY_HEIGHT,
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
            GuiComponent.blit(poseStack, BUFFER_X, BUFFER_Y, 0, BUFFER_DISPLAY_WIDTH, BUFFER_DISPLAY_HEIGHT, sprite);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }

        int localMouseX = mouseX - ((ModularRouterScreen) (Object) this).getGuiLeft();
        int localMouseY = mouseY - ((ModularRouterScreen) (Object) this).getGuiTop();

        if (localMouseX >= BUFFER_X && localMouseX < BUFFER_X + BUFFER_DISPLAY_WIDTH
                && localMouseY >= BUFFER_Y && localMouseY < BUFFER_Y + BUFFER_DISPLAY_HEIGHT) {
            List<Component> tooltip = provider.getBufferTooltip(router);
            if (!tooltip.isEmpty()) {
                this.renderComponentTooltip(poseStack, tooltip, localMouseX, localMouseY);
            }
        }
    }

    @Inject(method = {"mouseClicked", "m_6375_"}, at = @At("HEAD"), cancellable = true)
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
        PacketHandler.NETWORK.sendToServer(new BufferSlotInteractMessage(router.getBlockPos(), button == 0));
        cir.setReturnValue(true);
    }
}
