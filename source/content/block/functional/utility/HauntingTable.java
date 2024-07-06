package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.InventoryUtil;

public class HauntingTable extends Block {
	public HauntingTable(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == AoAItems.GHOULASM.get()) {
			if (player instanceof ServerPlayer serverPlayer) {
				if (!serverPlayer.getAbilities().instabuild)
					stack.shrink(1);

				InventoryUtil.giveItemTo(serverPlayer, AoAItems.PRIMED_GHOULASM);
				level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), AoASounds.BLOCK_HAUNTING_TABLE_USE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
