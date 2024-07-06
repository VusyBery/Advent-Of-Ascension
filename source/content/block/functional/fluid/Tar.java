package net.tslat.aoa3.content.block.functional.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Tar extends LiquidBlock {
	public Tar(Supplier<? extends FlowingFluid> supplier, Properties properties) {
		super(supplier.get(), properties);
	}

	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		return PathType.DAMAGE_FIRE;
	}

	@Override
	public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
		return PathType.DAMAGE_FIRE;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		float sizeSpeedMod = Mth.clamp(1 / (2 / (entity.getBbWidth() * entity.getBbHeight())), 0.35f, 1f);

		if (entity instanceof PathfinderMob mob && mob.getNavigation().getNodeEvaluator() instanceof FlyNodeEvaluator)
			sizeSpeedMod = Math.min(1, sizeSpeedMod * 1.5f);

		entity.makeStuckInBlock(state, new Vec3(sizeSpeedMod, sizeSpeedMod, sizeSpeedMod));
		entity.igniteForSeconds(2);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.01f && level.getBlockState(pos.above()).isAir())
			level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + random.nextFloat(), pos.getY() + 0.5f, pos.getZ() + random.nextFloat(), 0, 0.05f, 0);
	}
}