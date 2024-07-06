package net.tslat.aoa3.content.block.functional.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;

public class ClunkheadAltar extends BossAltarBlock {
	public ClunkheadAltar(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (getActivationItem() != null && stack.getItem() != getActivationItem())
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (!level.isClientSide)
				PlayerUtil.notifyPlayer(player, Component.translatable(LocaleUtil.createFeedbackLocaleKey("spawnBoss.difficultyFail")));

			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (player instanceof ServerPlayer) {
			if (getActivationItem() == null || stack.getItem() == getActivationItem()) {
				if (checkActivationConditions(player, hand, state, pos)) {
					ItemUtil.damageItemForUser(player, hand);
					doActivationEffect(player, hand, state, pos);
				}
			}
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	protected void doActivationEffect(Player player, InteractionHand hand, BlockState state, BlockPos blockPos) {
		/*ClunkheadEntity clunkhead = new ClunkheadEntity(AoAMobs.CLUNKHEAD.get(), player.level);

		clunkhead.teleportTo(blockPos.getX() - 1, blockPos.above().getY() + 1, blockPos.getZ() - 1);
		player.level.addFreshEntity(clunkhead);
		sendSpawnMessage(player, LocaleUtil.getLocaleMessage(AoAMobs.CLUNKHEAD.get().getDescriptionId() + ".spawn", player.getDisplayName()), blockPos);*/
	}

	@Override
	protected boolean checkActivationConditions(Player player, InteractionHand hand, BlockState state, BlockPos pos) {
		return WorldUtil.isWorld(player.level(), AoADimensions.RUNANDOR);
	}

	@Override
	protected Item getActivationItem() {
		return AoAItems.MEGA_RUNE_STONE.get();
	}
}
