package net.tslat.aoa3.content.entity.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BasicMiscEntity extends Entity implements GeoEntity {
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public BasicMiscEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean isPushable() {
		return isAlive();
	}

	@Override
	public boolean canBeCollidedWith() {
		return isAlive();
	}

	@Override
	public boolean isPickable() {
		return isAlive();
	}

	@Override
	public boolean canChangeDimensions(Level from, Level to) {
		return false;
	}

	@Override
	public void tick() {
		super.tick();

		if (!isNoGravity()) {
			double gravity = -0.08d;
			FluidType submergedFluid = getEyeInFluidType();

			if (submergedFluid != NeoForgeMod.EMPTY_TYPE.value())
				gravity *= getFluidMotionScale(submergedFluid);

			setDeltaMovement(getDeltaMovement().add(0, gravity, 0));
		}

		BlockPos feetPos = getBlockPosBelowThatAffectsMyMovement();
		float blockFriction = onGround() ? level().getBlockState(feetPos).getFriction(level(), feetPos, this) * 0.91f : 0.91f;

		setDeltaMovement(getDeltaMovement().multiply(blockFriction, 0.98d,  blockFriction));

		if (getDeltaMovement().lengthSqr() != 0)
			move(MoverType.SELF, getDeltaMovement());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
