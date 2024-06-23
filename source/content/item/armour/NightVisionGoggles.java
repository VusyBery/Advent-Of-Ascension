package net.tslat.aoa3.content.item.armour;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.RenderUtil;

import java.util.List;
import java.util.function.Consumer;

public class NightVisionGoggles extends AdventArmour {
	private static final ResourceLocation OVERLAY_TEXTURE = AdventOfAscension.id("textures/gui/overlay/helmet/night_vision_goggles.png");

	public NightVisionGoggles() {
		super(AoAArmourMaterials.NIGHT_VISION_GOGGLES, ArmorItem.Type.HELMET, 27);
	}

	@Override
	public Type getSetType() {
		return Type.ALL;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTick) {
				RenderUtil.renderFullscreenTexture(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()), OVERLAY_TEXTURE);
			}
		});
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(anySetEffectHeader());
	}
}
