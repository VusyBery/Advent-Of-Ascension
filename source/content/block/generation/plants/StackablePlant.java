package net.tslat.aoa3.content.block.generation.plants;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class StackablePlant extends Block implements IShearable {
	protected Supplier<? extends StackablePlant> hatBlock;
	protected Supplier<? extends StackablePlant> stemBlock;

	public StackablePlant(Block.Properties properties) {
		super(properties);

		stemBlock = () -> this;
		hatBlock = () -> this;
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos newPos;
		BlockState blockState = world.getBlockState(newPos = pos.above());
		Block block = blockState.getBlock();

		while (block == this.stemBlock.get() || block == this.hatBlock.get()) {
			world.setBlock(newPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
			world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));

			if (!world.isClientSide() && !player.isCreative()) {
				dropResources(state, world, pos, null, player, player.getMainHandItem());
				dropResources(blockState, world, newPos, null, player, player.getMainHandItem());
			}

			blockState = world.getBlockState(newPos = newPos.above());
			block = blockState.getBlock();
		}

		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState targetState = level.getBlockState(pos.below());

		return targetState.getBlock() == this.stemBlock.get() || targetState.canSustainPlant(level, pos.below(), Direction.UP, state) != TriState.FALSE;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, world, pos, facingPos);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return false;
	}

	@Override
	public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return type == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, type);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
		return ObjectArrayList.of(asItem().getDefaultInstance());
	}
}
