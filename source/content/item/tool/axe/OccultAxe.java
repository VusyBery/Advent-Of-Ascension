package net.tslat.aoa3.content.item.tool.axe;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class OccultAxe extends BaseAxe {
	public OccultAxe(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
		if (entity instanceof Player && state.is(BlockTags.LOGS)) {
			BlockPos breakPos = pos;
			Block originBlock = state.getBlock();
			ItemStack toolStack = entity.getItemInHand(InteractionHand.MAIN_HAND);

			while (world.getBlockState(breakPos = breakPos.above()).getBlock() == originBlock && !toolStack.isEmpty()) {
				WorldUtil.harvestAdditionalBlock(world, (Player)entity, breakPos, false);
			}
		}

		return super.mineBlock(stack, world, state, pos, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}
}
