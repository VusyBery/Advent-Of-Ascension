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


public class GoblinEntity extends AoAMeleeMob<GoblinEntity> {
	public GoblinEntity(EntityType<? extends GoblinEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_GOBLIN_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_GOBLIN_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_GOBLIN_HURT.get();
	}

	@Override
	protected int getAttackSwingDuration() {
		return 28;
	}

	@Override
	protected int getPreAttackTime() {
		return 13;
	}

	public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
		return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT_DAY_MONSTER;
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<GoblinEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(20)
				.moveSpeed(0.29)
				.meleeStrength(4)
				.followRange(12)
				.aggroRange(8);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				DefaultAnimations.genericWalkController(this),
				AoAAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING));
	}
}
