package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.content.world.teleporter.AoAPortal;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
	public BlockPos retrieveExistingLinkExit(ServerPlayer player, ServerLevel currentWorld, ServerLevel destWorld, GlobalPos existingLink) {
		if (WorldUtil.isWorld((Level)currentWorld, AoADimensions.NOWHERE) && player.distanceToSqr(17, 453, 1) < 100) {
			GlobalPos returnLoc = PlayerUtil.getAdventPlayer(player).storage.getPortalReturnFor(AoADimensions.NOWHERE);

			if (returnLoc != null)
				return returnLoc.pos();
		}

		return super.retrieveExistingLinkExit(player, currentWorld, destWorld, existingLink);
	}

	@Override
	public BlockPos findExistingPortal(Level targetLevel, Entity entity, BlockPos originPos) {
		if (WorldUtil.isWorld(targetLevel, AoADimensions.NOWHERE))
			return new BlockPos(25, 1501, 15);

		return super.findExistingPortal(targetLevel, entity, originPos);
	}

	@Override
	public BlockPos findSuitablePortalLocation(Level level, Entity entity, BlockPos originPos) {
		if (WorldUtil.isWorld(level, Level.OVERWORLD))
			return level.getSharedSpawnPos();

		return super.findSuitablePortalLocation(level, entity, originPos);
	}

	@Override
	public DimensionTransition getTransitionForPortalLink(ServerLevel targetLevel, Entity entity, Optional<BlockPos> fromPortal, BlockPos safeCoords, Optional<GlobalPos> existingLink) {
		final ServerLevel fromLevel = (ServerLevel)entity.level();
		final BlockPos portalPos = AoAPortal.getOrCreatePortalLocation(targetLevel, fromLevel, entity, safeCoords, this, existingLink);

		fromPortal.ifPresent(portalBlock -> {
			if (entity instanceof ServerPlayer pl && (!WorldUtil.isWorld((Level)fromLevel, AoADimensions.NOWHERE) || fromLevel.structureManager().getStructureAt(portalBlock, fromLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, AdventOfAscension.id("nowhere_disabled_boss_lobby")))) == StructureStart.INVALID_START))
				AoAPortal.updatePlayerLink(pl, portalBlock, fromLevel.dimension(), targetLevel.dimension());
		});

		return new DimensionTransition(targetLevel, Vec3.atCenterOf(portalPos), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), this.playTransitSound(entity).then(DimensionTransition.PLACE_PORTAL_TICKET));
	}
}