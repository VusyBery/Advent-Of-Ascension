package net.tslat.aoa3.content.item.tool.pickaxe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.tslat.aoa3.advent.AoAResourceCaching;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.event.GlobalEvents;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class OccultPickaxe extends BasePickaxe {
	public static final Reference2ObjectOpenHashMap<Block, ColourUtil.Colour> BLOCK_COLOUR_MAP = Util.make(new Reference2ObjectOpenHashMap<>(), cache -> AoAResourceCaching.onClientLogoutAndReload(registryAccess -> cache.clear()));

	public OccultPickaxe(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			List<LocatedBlock> blocks = new ObjectArrayList<>();

			for (int i = (int)(player.getX() - 4.0); i < (int)(player.getX() + 4.0); ++i) {
				for (int j = (int)(player.getY() - 4.0); j < (int)(player.getY() + 4.0); ++j) {
					for (int k = (int)(player.getZ() - 4.0); k < (int)(player.getZ() + 4.0); ++k) {
						BlockPos pos = new BlockPos(i, j, k);
						BlockState state = level.getBlockState(pos);

						if (state.is(Tags.Blocks.ORES))
							blocks.add(new LocatedBlock(level, pos, state));
					}
				}
			}

			ClientOperations.addOccultBlocks(GlobalEvents.tick + 150, blocks);
		}

		InteractionResultHolder<ItemStack> result = super.use(level, player, hand);

		if (result.getResult() == InteractionResult.FAIL)
			return InteractionResultHolder.pass(result.getObject());

		return result;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}

	public record LocatedBlock(ColourUtil.Colour colour, BlockPos pos, BlockState state) {
		private static final TagKey<Block>[] TIERS = new TagKey[] {BlockTags.INCORRECT_FOR_WOODEN_TOOL, BlockTags.INCORRECT_FOR_STONE_TOOL, BlockTags.INCORRECT_FOR_IRON_TOOL, BlockTags.INCORRECT_FOR_GOLD_TOOL, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, BlockTags.INCORRECT_FOR_NETHERITE_TOOL};

		public LocatedBlock(Level level, BlockPos pos, BlockState state) {
			this(getColourForBlock(level, state), pos, state);
		}

		public static ColourUtil.Colour getColourForBlock(Level level, BlockState blockState) {
			return BLOCK_COLOUR_MAP.computeIfAbsent(blockState.getBlock(), block -> {
				for (int i = TIERS.length - 1; i >= 0; i--) {
					if (blockState.is(TIERS[i]))
						return new ColourUtil.Colour(1 - (i / (float)TIERS.length), i / (float)TIERS.length, 0, 0.85f);
				}

				return new ColourUtil.Colour(1, 0, 0, 0.85f);
			});
		}
	}
}
