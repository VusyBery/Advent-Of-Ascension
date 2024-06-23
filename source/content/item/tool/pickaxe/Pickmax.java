package net.tslat.aoa3.content.item.tool.pickaxe;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class Pickmax extends BasePickaxe {
	public Pickmax(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
		if (!world.isClientSide && entity instanceof Player pl && !(entity instanceof FakePlayer) && (state.is(Tags.Blocks.STONES) || state.is(Tags.Blocks.COBBLESTONES))) {
			for (BlockPos breakPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
				if (breakPos.equals(pos))
					continue;

				BlockState extraBlock = world.getBlockState(breakPos);

				if ((extraBlock.is(Tags.Blocks.STONES) || extraBlock.is(Tags.Blocks.COBBLESTONES)) && world.getBlockState(pos).getDestroyProgress(pl, world, pos) / extraBlock.getDestroyProgress(pl, world, breakPos) < 10f)
					WorldUtil.harvestAdditionalBlock(world, pl, breakPos, false);
			}
		}

		return super.mineBlock(stack, world, state, pos, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}
}
