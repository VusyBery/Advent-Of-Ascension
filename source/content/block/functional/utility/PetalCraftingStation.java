package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAArmour;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.smartbrainlib.util.RandomUtil;

public class PetalCraftingStation extends Block {
	public PetalCraftingStation(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == AoAItems.PETALS.get()) {
			if (!level.isClientSide()) {
				if (!player.isCreative())
					stack.shrink(1);

				ItemUtil.givePlayerItemOrDrop(player, RandomUtil.getRandomSelection(
						AoAArmour.HYDRANGIC_ARMOUR.boots,
						AoAArmour.HYDRANGIC_ARMOUR.leggings,
						AoAArmour.HYDRANGIC_ARMOUR.chestplate,
						AoAArmour.HYDRANGIC_ARMOUR.helmet).get().getDefaultInstance());

				level.playSound(null, pos, AoASounds.BLOCK_PETAL_CRAFTING_STATION_USE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
