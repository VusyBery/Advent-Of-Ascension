package net.tslat.aoa3.content.block.functional.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.item.AoAItems;

public class DustopianLamp extends Block {
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	public DustopianLamp(BlockBehaviour.Properties properties) {
		super(properties);

		registerDefaultState(defaultBlockState().setValue(LIT, false));
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(LIT) ? 14 : 0;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(AoAItems.DARKLY_POWDER) && !state.getValue(LIT)) {
			if (!level.isClientSide()) {
				if (!player.getAbilities().instabuild)
					stack.shrink(1);

				level.setBlockAndUpdate(pos, defaultBlockState().setValue(LIT, true));
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT);
	}
}
