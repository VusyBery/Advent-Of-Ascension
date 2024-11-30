package net.tslat.aoa3.content.entity.monster.overworld;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class GhostEntity extends AoAMeleeMob<GhostEntity> {
	public GhostEntity(EntityType<? extends GhostEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState) {}

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

	public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
		return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT_MONSTER.noHigherThanY(0).spawnChance(1 / 2f);
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<GhostEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(15)
				.moveSpeed(0.2875)
				.meleeStrength(4)
				.knockbackResist(1)
				.followRange(10)
				.aggroRange(8);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				DefaultAnimations.genericIdleController(this),
				DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING).transitionLength(0));
	}
}
