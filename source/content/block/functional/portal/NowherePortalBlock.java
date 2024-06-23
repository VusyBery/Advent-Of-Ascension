package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.content.world.teleporter.PortalCoordinatesContainer;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

public class NowherePortalBlock extends PortalBlock {
	public NowherePortalBlock(BlockBehaviour.Properties properties) {
		super(properties, AoADimensions.NOWHERE, ColourUtil.RGB(255, 227, 117), AoASounds.BLOCK_NOWHERE_PORTAL_AMBIENT);
	}

	@Override
	public Block getPortalFrame() {
		return AoABlocks.ANCIENT_ROCK.get();
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof Player || (entity instanceof TamableAnimal animal && animal.isTame()))
			super.entityInside(state, world, pos, entity);
	}

	@Nullable
	@Override
	public BlockPos retrieveExistingLinkExit(ServerPlayer player, ServerLevel currentWorld, ServerLevel destWorld, PortalCoordinatesContainer existingLink) {
		if (WorldUtil.isWorld((Level)currentWorld, AoADimensions.NOWHERE) && player.distanceToSqr(17, 453, 1) < 100) {
			PortalCoordinatesContainer returnLoc = PlayerUtil.getAdventPlayer(player).getPortalReturnLocation(AoADimensions.NOWHERE);

			if (returnLoc != null)
				return returnLoc.portalPos();
		}

		return super.retrieveExistingLinkExit(player, currentWorld, destWorld, existingLink);
	}

	@Override
	public BlockPos findExistingPortal(Level targetLevel, Entity entity, BlockPos originPos) {
		if (WorldUtil.isWorld(targetLevel, AoADimensions.NOWHERE))
			return new BlockPos(25, 1501, 15);

		return super.findExistingPortal(targetLevel, entity, originPos);
	}
}