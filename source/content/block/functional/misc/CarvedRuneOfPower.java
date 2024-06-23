package net.tslat.aoa3.content.block.functional.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.content.block.functional.portal.PortalBlock;
import net.tslat.aoa3.content.item.misc.BlankRealmstone;
import net.tslat.aoa3.content.item.misc.Realmstone;
import net.tslat.aoa3.content.world.teleporter.AoAPortalFrame;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;


public class CarvedRuneOfPower extends Block {
	public CarvedRuneOfPower(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() instanceof Realmstone)
			return fillPortal(level, pos, hitResult.getDirection(), stack, player);

		if (stack.getItem() instanceof BlankRealmstone)
			return clearPortal(level, pos, hitResult.getDirection(), stack, player);

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public static ItemInteractionResult fillPortal(Level level, BlockPos pos, Direction direction, ItemStack stack, @Nullable Player player) {
		Realmstone realmstone = (Realmstone)stack.getItem();

		if (realmstone.getPortalBlock() == null) {
			if (!level.isClientSide)
				player.sendSystemMessage(LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("portal.tba")));

			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}

		PortalBlock portalBlock = (PortalBlock)realmstone.getPortalBlock().get();
		AoAPortalFrame.PortalDirection facing = AoAPortalFrame.testFrameForActivation(level, pos, direction, portalBlock);

		if (facing == AoAPortalFrame.PortalDirection.EXISTING) {
			if (!level.isClientSide)
				player.sendSystemMessage(LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("teleporterFrame.existing")));

			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}

		if (facing == AoAPortalFrame.PortalDirection.INVALID) {
			if (!level.isClientSide)
				player.sendSystemMessage(LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("teleporterFrame.fail")));

			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		}

		if (level instanceof ServerLevel serverLevel) {
			AoAPortalFrame.lightPortalFrame(serverLevel, pos, facing, portalBlock);
			level.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), AoASounds.PORTAL_ACTIVATE.get(), SoundSource.AMBIENT, 1, 1);
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	public static ItemInteractionResult clearPortal(Level level, BlockPos pos, Direction direction, ItemStack stack, @Nullable Player player) {
		if (level.getBlockState(pos.relative(Direction.UP)).getBlock() instanceof PortalBlock) {
			if (!level.isClientSide)
				level.setBlockAndUpdate(pos.relative(Direction.UP), Blocks.AIR.defaultBlockState());

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.FAIL;
	}
}