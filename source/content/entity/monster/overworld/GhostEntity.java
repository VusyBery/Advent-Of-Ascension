package net.tslat.aoa3.content.entity.monster.overworld;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.client.render.AoAAnimations;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class GhostEntity extends AoAMeleeMob<GhostEntity> {
	public GhostEntity(EntityType<? extends GhostEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound(BlockPos pos, BlockState blockState) {
		return null;
	}

	@Override
	public boolean isAffectedByPotions() {
		return false;
	}

	@Override
	protected int getAttackSwingDuration() {
		return 11;
	}

	@Override
	protected int getPreAttackTime() {
		return 3;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				DefaultAnimations.genericIdleController(this),
				AoAAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING));
	}
}
