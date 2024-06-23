package net.tslat.aoa3.content.item.tool.shovel;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class OrnamyteShovel extends BaseShovel {
	public OrnamyteShovel(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return state.is(AoATags.Blocks.GRASS) || state.getBlock() instanceof GrassBlock ? super.getDestroySpeed(stack, state) * 10 : super.getDestroySpeed(stack, state);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}
}
