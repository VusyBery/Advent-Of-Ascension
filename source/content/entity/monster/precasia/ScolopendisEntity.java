package net.tslat.aoa3.content.entity.monster.precasia;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class ScolopendisEntity extends AoAMeleeMob<ScolopendisEntity> {
	public ScolopendisEntity(EntityType<? extends ScolopendisEntity> entityType, Level level) {
		super(entityType, level);

		setParts(new AoAEntityPart<>(this, getBbWidth(), getBbHeight(), 0, 0, getBbWidth()),
				new AoAEntityPart<>(this, getBbWidth(), getBbHeight(), 0, 0, -getBbWidth()));
	}

	@Override
	protected double getAttackReach() {
		return 1.75f;
	}

	@Override
	protected int getAttackSwingDuration() {
		return 13;
	}

	@Override
	protected int getPreAttackTime() {
		return 8;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_SCOLOPENDIS_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return AoASounds.ENTITY_SCOLOPENDIS_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_SCOLOPENDIS_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound(BlockPos pos, BlockState blockState) {
		return SoundEvents.SPIDER_STEP;
	}

	@Override
	protected void onAttack(Entity target) {
		if (target instanceof LivingEntity livingTarget && RandomUtil.oneInNChance(10))
			EntityUtil.applyPotions(livingTarget, new EffectBuilder(MobEffects.CONFUSION, 400).hideParticles().isAmbient().level(1));

	}

	@Override
	public BrainActivityGroup<? extends ScolopendisEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> !DamageUtil.isAttackable(target) || distanceToSqr(target.position()) > Math.pow(getAttributeValue(Attributes.FOLLOW_RANGE), 2)),
				new SetWalkTargetToAttackTarget<>().speedMod((entity, target) -> entity.distanceToSqr(target) < 16 ? 1 : 1.25f),
				new AnimatableMeleeAttack<>(getPreAttackTime()).attackInterval(entity -> getAttackSwingDuration() + 2));
	}

	public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
		return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.and((entityType, level, spawnType, pos, rand) -> {
			if (level.getLevel().dimension() != AoADimensions.PRECASIA || spawnType != MobSpawnType.NATURAL)
				return true;

			return pos.getY() <= 50 && rand.nextFloat() < 0.025f;
		});
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<ScolopendisEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(43)
				.moveSpeed(0.33)
				.meleeStrength(8.5f)
				.aggroRange(16)
				.followRange(32);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(DefaultAnimations.genericWalkRunIdleController(this));
		controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_BITE).transitionLength(0));
	}
}
