package com.yuuhamu.routerupgradecore.mixin;

import com.yuuhamu.routerupgradecore.api.RouterModeProvider;
import com.yuuhamu.routerupgradecore.internal.ModeRegistry;
import me.desht.modularrouters.block.tile.ModularRouterBlockEntity;
import me.desht.modularrouters.client.gui.ModularRouterScreen;
import me.desht.modularrouters.container.RouterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModularRouterScreen.class, remap = false)
public abstract class ModularRouterScreenMixin {

    @Shadow
    protected AbstractContainerMenu menu;

    @Redirect(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    private void routerupgradecore$redirectBlit(GuiGraphics graphics, ResourceLocation texture,
                                                 int x, int y, int u, int v, int width, int height) {
        ModularRouterBlockEntity router = ((RouterMenu) this.menu).getRouter();
        RouterModeProvider provider = ModeRegistry.getActiveProvider(router);
        ResourceLocation override = provider == null ? null : provider.getGuiTexture();
        graphics.blit(override != null ? override : texture, x, y, u, v, width, height);
    }
}
