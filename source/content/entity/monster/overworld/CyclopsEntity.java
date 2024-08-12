package net.tslat.aoa3.content.entity.monster.overworld;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.client.render.AoAAnimations;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;


public class CyclopsEntity extends AoAMeleeMob<CyclopsEntity> {
	public CyclopsEntity(EntityType<? extends CyclopsEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_CYCLOPS_AMBIENT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_CYCLOPS_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_CYCLOPS_HURT.get();
	}

	@Override
	protected int getAttackSwingDuration() {
		return 20;
	}

	@Override
	protected int getPreAttackTime() {
		return 8;
	}

	public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
		return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(55);
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<CyclopsEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(25)
				.moveSpeed(0.2875)
				.meleeStrength(4)
				.followRange(14)
				.aggroRange(14);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				DefaultAnimations.genericWalkIdleController(this),
				AoAAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING));
	}
}
