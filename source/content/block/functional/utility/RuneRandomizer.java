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
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LootUtil;

public class RuneRandomizer extends Block {
	public RuneRandomizer(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == AoAItems.UNPOWERED_RUNE.get() || stack.getItem() == AoAItems.CHARGED_RUNE.get()) {
			if (player instanceof ServerPlayer serverPlayer) {
				if (!serverPlayer.isCreative())
					stack.shrink(1);

				InventoryUtil.giveItemsTo(serverPlayer, LootUtil.generateLoot(AdventOfAscension.id("misc/rune_randomizer"), LootUtil.getGiftParameters(serverPlayer.serverLevel(), Vec3.atLowerCornerOf(pos), player)));

				serverPlayer.level().playSound(null, pos.getX(), pos.getY(), pos.getZ(), AoASounds.BLOCK_RUNE_RANDOMIZER_USE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
