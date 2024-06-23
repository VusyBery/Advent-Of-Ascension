package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.menu.MendingTableMenu;
import net.tslat.aoa3.util.InteractionResults;

public class MendingTable extends Block {
	public MendingTable(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player instanceof ServerPlayer)
			MendingTableMenu.openContainer((ServerPlayer)player, pos);

		return InteractionResults.BlockUseWithoutItem.succeedAndSwingArmBothSides(level.isClientSide);
	}
}
