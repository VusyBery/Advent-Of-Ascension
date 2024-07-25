package net.tslat.aoa3.client.clientextension.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.util.RenderUtil;

public class NightVisionGogglesClientExtension implements IClientItemExtensions {
    private static final ResourceLocation OVERLAY_TEXTURE = AdventOfAscension.id("textures/gui/overlay/helmet/night_vision_goggles.png");

    @Override
    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTick) {
        RenderUtil.renderFullscreenTexture(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()), OVERLAY_TEXTURE);
    }
}
