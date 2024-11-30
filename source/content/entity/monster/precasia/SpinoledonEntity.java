package net.tslat.aoa3.content.entity.monster.precasia;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.render.AoAAnimations;
import net.tslat.aoa3.common.registration.AoAAttributes;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.block.AoAFluidTypes;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.common.registration.entity.AoAMonsters;
import net.tslat.aoa3.content.entity.animal.precasia.DeinotheriumEntity;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyLivingEntitySensor;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyPlayersSensor;
import net.tslat.aoa3.library.object.EntityDataHolder;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntitySpawningUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.List;

public class SpinoledonEntity extends AoAMeleeMob<SpinoledonEntity> {
	public static final EntityDataHolder<Boolean> LUNGING = EntityDataHolder.register(SpinoledonEntity.class, EntityDataSerializers.BOOLEAN, false, entity -> entity.lunging, (entity, value) -> entity.lunging = value);
	private static final AttributeModifier LUNGE_DAMAGE_BONUS = new AttributeModifier(AdventOfAscension.id("lunge_damage"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	private boolean lunging = false;

	public SpinoledonEntity(EntityType<? extends SpinoledonEntity> entityType, Level world) {
		super(entityType, world);

		setParts(new AoAEntityPart<>(this, getBbWidth(), 14 / 16f, 0, 17 / 16f, getBbWidth()),
				new AoAEntityPart<>(this, 8 / 16f, 13 / 16f, 0, 22 / 16f, getBbWidth() + 0.625f).setDamageMultiplier(1.1f),
				new AoAEntityPart<>(this, 8 / 16f, 0.5f, 0, 25 / 16f, getBbWidth() + 1.125f).setDamageMultiplier(1.1f),
				new AoAEntityPart<>(this, 8 / 16f, 0.5f, 0, 25 / 16f, getBbWidth() + 1.625f).setDamageMultiplier(1.25f),
				new AoAEntityPart<>(this, getBbWidth(), 10 / 16f, 0, 19 / 16f, -getBbWidth()).setDamageMultiplier(0.9f),
				new AoAEntityPart<>(this, 10 / 16f, 9 / 16f, 0, 18 / 16f, -getBbWidth() - 0.6875f).setDamageMultiplier(0.75f));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);

		registerDataParams(builder, LUNGING);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (this.tickCount % 100 == 0) {
			if (!level().isDay()) {
				AttributeUtil.applyTransientModifier(this, AoAAttributes.AGGRO_RANGE, AoAAttributes.NIGHT_AGGRO_MODIFIER);
			}
			else {
				AttributeUtil.removeModifier(this, AoAAttributes.AGGRO_RANGE, AoAAttributes.NIGHT_AGGRO_MODIFIER);
			}
		}
	}

	@Override
	public List<ExtendedSensor<? extends SpinoledonEntity>> getSensors() {
		return ObjectArrayList.of(
				new AggroBasedNearbyPlayersSensor<>(),
				new AggroBasedNearbyLivingEntitySensor<SpinoledonEntity>()
						.setPredicate((target, entity) -> (target instanceof OwnableEntity tamedEntity && tamedEntity.getOwnerUUID() != null) || target instanceof DeinotheriumEntity)
						.setScanRate(entity -> 40),
				new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<? extends SpinoledonEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new TargetOrRetaliate<>()
						.useMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
						.attackablePredicate(target -> DamageUtil.isAttackable(target) && !isAlliedTo(target)),
				new OneRandomBehaviour<>(
						new SetRandomWalkTarget<>().speedModifier(0.9f),
						new Idle<>().runFor(entity -> entity.level().isDay() ? entity.getRandom().nextInt(30, 60) : entity.getRandom().nextInt(60, 120))));
	}

	@Override
	public BrainActivityGroup<? extends SpinoledonEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((entity, target) -> !DamageUtil.isAttackable(target) || distanceToSqr(target.position()) > Math.pow(getAttributeValue(Attributes.FOLLOW_RANGE), 2)),
				new SetWalkTargetToAttackTarget<>().closeEnoughDist((entity, target) -> 2),
				new AnimatableMeleeAttack<>(getPreAttackTime())
						.attackInterval(entity -> getAttackSwingDuration())
						.whenStarting(entity -> {
							LUNGING.set(entity, entity.distanceToSqr(BrainUtils.getTargetOfEntity(entity)) < 4);

							if (LUNGING.is(entity, true)) {
								AttributeUtil.applyTransientModifier(entity, Attributes.ATTACK_DAMAGE, LUNGE_DAMAGE_BONUS);
							}
							else {
								AttributeUtil.removeModifier(entity, Attributes.ATTACK_DAMAGE, LUNGE_DAMAGE_BONUS);
							}
						}));
	}

	@Override
	public int getMaxHeadXRot() {
		return 65;
	}

	@Override
	public int getMaxHeadYRot() {
		return 40;
	}

	@Override
	public int getHeadRotSpeed() {
		return 40;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_SPINOLEDON_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_SPINOLEDON_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_SPINOLEDON_HURT.get();
	}

	@Override
	protected float getStepWeight() {
		return 5f;
	}

	@Override
	protected float nextStep() {
		return this.moveDist + 1.4f;
	}

	@Override
	public int getCurrentSwingDuration() {
		return this.lunging ? 24 : 11;
	}

	@Override
	protected int getPreAttackTime() {
		return this.lunging ? 13 : 4;
	}

	@Override
	protected double getAttackReach() {
		return 3.05f;
	}

	@Override
	protected void onAttack(Entity target) {
		if (target instanceof Animal animal && animal.getHealth() <= 0)
			heal(animal.getMaxHealth() / 10f);
	}

	@Override
	public void onDamageTaken(DamageContainer damageContainer) {
		if (level() instanceof ServerLevel level && damageContainer.getSource().is(DamageTypeTags.IS_FIRE) && level().getFluidState(BlockPos.containing(getEyePosition())).getFluidType() == AoAFluidTypes.TAR.get() && level().getFluidState(blockPosition().above()).getFluidType() == AoAFluidTypes.TAR.get()) {
			ParticleBuilder.forRandomPosInEntity(ParticleTypes.LARGE_SMOKE, this)
					.colourOverride(255, 255, 255, 255)
					.spawnNTimes(20)
					.sendToAllPlayersTrackingEntity(level,this);

			if (isDeadOrDying()) {
				AoAScheduler.scheduleSyncronisedTask(() -> {
					EntitySpawningUtil.spawnEntity(level, AoAMonsters.SKELETAL_ABOMINATION.get(), position(), MobSpawnType.CONVERSION, abomination -> {
						abomination.setXRot(getXRot());
						abomination.setYRot(getYRot());
						abomination.setYHeadRot(getYHeadRot());
					});
				}, 19 - this.deathTime);
			}
		}
	}

	public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
		return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.and((entityType, level, spawnType, pos, random) -> pos.getY() >= 60 || pos.getY() <= -13).difficultyBasedSpawnChance(0.05f);
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<SpinoledonEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(51)
				.armour(4, 4)
				.moveSpeed(0.3)
				.meleeStrength(9.5f)
				.knockbackResist(0.7f)
				.aggroRange(16)
				.followRange(32);
	}

	@Override
	protected int getAttackSwingDuration() {
		return this.lunging ? 25 : 11;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(DefaultAnimations.genericWalkRunIdleController(this));
		controllers.add(new AnimationController<GeoAnimatable>(this, "Attack", 0, state -> {
			if (this.swinging) {
				if (this.lunging) {
					state.setControllerSpeed(2f);

					return state.setAndContinue(AoAAnimations.ATTACK_POUNCE);
				}

				state.setControllerSpeed(1);

				return state.setAndContinue(DefaultAnimations.ATTACK_BITE);
			}

			state.resetCurrentAnimation();

			return PlayState.STOP;
		}));
	}
}
