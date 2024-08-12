package net.tslat.aoa3.content.entity.base;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.tslat.aoa3.content.entity.ai.movehelper.MultiFluidSmoothGroundNavigation;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyLivingEntitySensor;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyPlayersSensor;
import net.tslat.aoa3.library.object.EntityDataHolder;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public abstract class AoAMonster<T extends AoAMonster<T>> extends Monster implements GeoEntity, SmartBrainOwner<T>, AoAMultipartEntity {
	public static final EntityDataHolder<Integer> ATTACK_STATE = EntityDataHolder.register(AoAMonster.class, EntityDataSerializers.INT, 0, monster -> monster.attackState, (monster, value) -> monster.attackState = value);
	public static final EntityDataHolder<Boolean> INVULNERABLE = EntityDataHolder.register(AoAMonster.class, EntityDataSerializers.BOOLEAN, false, Entity::isInvulnerable, AoAMonster::setInvulnerable);
	public static final EntityDataHolder<Boolean> IMMOBILE = EntityDataHolder.register(AoAMonster.class, EntityDataSerializers.BOOLEAN, false, monster -> monster.immobile, (monster, value) -> monster.immobile = value);

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected AoAEntityPart<?>[] parts = new AoAEntityPart[0];
	private EntityDataHolder<?>[] dataParams;

	protected boolean hasDrops = true;
	private int attackState = 0;
	private boolean immobile = false;

	protected AoAMonster(EntityType<? extends AoAMonster> entityType, Level level) {
		super(entityType, level);

		getNavigation().setCanFloat(true);
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new MultiFluidSmoothGroundNavigation(this, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);

		this.dataParams = new EntityDataHolder<?>[] {ATTACK_STATE, INVULNERABLE, IMMOBILE};

		for (EntityDataHolder<?> dataHolder : this.dataParams) {
			dataHolder.defineDefault(builder);
		}
	}

	protected final void registerDataParams(SynchedEntityData.Builder builder, EntityDataHolder<?>... params) {
		EntityDataHolder<?>[] newArray = new EntityDataHolder[this.dataParams.length + params.length];

		System.arraycopy(this.dataParams, 0, newArray, 0, this.dataParams.length);
		System.arraycopy(params, 0, newArray, this.dataParams.length, params.length);

		for (EntityDataHolder<?> param : params) {
			param.defineDefault(builder);
		}

		this.dataParams = newArray;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return null;
	}

	@Nullable
	protected SoundEvent getStepSound(BlockPos pos, BlockState blockState) {
		return null;
	}

	@Override
	protected float nextStep() {
		return this.moveDist + 1;
	}

	protected float getStepWeight() {
		return 1f;
	}

	protected boolean isQuadruped() {
		return false;
	}

	@Override
	public int getAmbientSoundInterval() {
		return 240;
	}

	public final RandomUtil.EasyRandom rand() {
		return new RandomUtil.EasyRandom(getRandom());
	}

	@Override
	protected Brain.Provider<T> brainProvider() {
		return new SmartBrainProvider(this);
	}

	@Override
	public List<ExtendedSensor<? extends T>> getSensors() {
		return ObjectArrayList.of(
				new AggroBasedNearbyPlayersSensor<T>(),
				new AggroBasedNearbyLivingEntitySensor<T>().setPredicate((target, entity) -> target instanceof OwnableEntity tamedEntity && tamedEntity.getOwnerUUID() != null).setScanRate(entity -> 40),
				new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<? extends T> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new LookAtTarget<>(),
				new WalkOrRunToWalkTarget<>().startCondition(entity -> !isDoingStationaryActivity()),
				new FloatToSurfaceOfFluid<>());
	}

	@Override
	public BrainActivityGroup<? extends T> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new TargetOrRetaliate<>()
						.useMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
						.attackablePredicate(target -> target.isAlive() && (!(target instanceof Player player) || !player.getAbilities().invulnerable) && !isAlliedTo(target)),
				new OneRandomBehaviour<>(
						new SetRandomWalkTarget<>().speedModifier(0.9f),
						new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	protected int getAttackSwingDuration() {
		return 6;
	}

	protected int getPreAttackTime() {
		return 0;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnReason) {
		return true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		this.hasDrops = spawnType != MobSpawnType.MOB_SUMMONED;
		this.xpReward = calculateKillXp();

		if (spawnType == MobSpawnType.SPAWNER)
			this.xpReward *= 0.5d;

		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	public int calculateKillXp() {
		return !this.hasDrops ? 0 : (int)(5 + (getAttributeValue(Attributes.MAX_HEALTH) + getAttributeValue(Attributes.ARMOR) * 1.75f + getAttributeValue(Attributes.ARMOR_TOUGHNESS) * 1.5f) / 10f);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState) {
		if (!blockState.liquid()) {
			BlockState state = level().getBlockState(pos.above());
			SoundType blockSound = state.getBlock() == Blocks.SNOW ? state.getSoundType(level(), pos, this) : blockState.getSoundType(level(), pos, this);
			SoundEvent stepSound = blockSound.getStepSound();
			SoundEvent stepSoundOverlay = getStepSound(pos, blockState);

			playStepSounds(stepSound, stepSoundOverlay);

			if (isQuadruped() && !level().isClientSide)
				AoAScheduler.scheduleSyncronisedTask(() -> playStepSounds(stepSound, stepSoundOverlay), 6);
		}
	}

	private void playStepSounds(SoundEvent stepSound, @Nullable SoundEvent stepSoundOverlay) {
		float stepWeight = getStepWeight() - 1;

		playSound(stepSound, 5 * 0.15f + stepWeight * 0.15f, 1 - stepWeight * 0.1f);

		if (stepSoundOverlay != null)
			playSound(stepSoundOverlay, 5 * 0.15f + stepWeight * 0.15f, 1 - stepWeight * 0.1f);
	}

	@Override
	public int getCurrentSwingDuration() {
		int time = getAttackSwingDuration();

		if (MobEffectUtil.hasDigSpeed(this))
			time -= 1 + MobEffectUtil.getDigSpeedAmplification(this);

		if (hasEffect(MobEffects.DIG_SLOWDOWN))
			time += (1 + getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2;

		return time;
	}

	@Override
	protected void customServerAiStep() {
		tickBrain((T)this);
	}

	@Nullable
	@Override
	public LivingEntity getTarget() {
		return BrainUtils.getTargetOfEntity(this, super.getTarget());
	}

	public void setImmobile(boolean immobile) {
		IMMOBILE.set(this, immobile);

		if (this.immobile)
			getNavigation().stop();
	}

	public boolean isDoingStationaryActivity() {
		return this.immobile;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.parts.length > 0 && source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
			if (arrow.piercingIgnoreEntityIds == null)
				arrow.piercingIgnoreEntityIds = new IntOpenHashSet(5);

			for (AoAEntityPart<?> part : this.parts) {
				arrow.piercingIgnoreEntityIds.add(part.getId());
			}

			arrow.piercingIgnoreEntityIds.add(getId());
		}

		return super.hurt(source, amount);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		float damage = (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
		DamageSource damageSource = damageSources().mobAttack(this);

		if (level() instanceof ServerLevel serverLevel)
			damage = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), target, damageSource, damage);

		if (target.hurt(damageSource, damage)) {
			if (target instanceof LivingEntity livingTarget) {
				float knockback = getKnockback(target, damageSource);

				if (knockback > 0) {
					livingTarget.knockback(knockback * 0.5f, Mth.sin(getYRot() * Mth.DEG_TO_RAD), -Mth.cos(getYRot() * Mth.DEG_TO_RAD));
					setDeltaMovement(getDeltaMovement().multiply(0.6, 1.0, 0.6));
				}
			}

			if (level() instanceof ServerLevel serverLevel)
				EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);

			setLastHurtMob(target);
			playAttackSound();
			onAttack(target);

			return true;
		}

		return false;
	}

	protected DamageSource getAttackDamageSource(Entity target) {
		return damageSources().mobAttack(this);
	}

	protected void onAttack(Entity target) {}

	@Override
	public void die(DamageSource source) {
		if (CommonHooks.onLivingDeath(this, source))
			return;

		if (!isRemoved() && !this.dead) {
			Entity lastAttacker = source.getEntity();
			LivingEntity killer = getKillCredit();

			if (this.deathScore >= 0 && killer != null)
				killer.awardKillScore(this, this.deathScore, source);

			if (isSleeping())
				stopSleeping();

			this.dead = true;

			if (level() instanceof ServerLevel serverLevel) {
				if (lastAttacker == null || lastAttacker.killedEntity(serverLevel, this)) {
					gameEvent(GameEvent.ENTITY_DIE);
					dropAllDeathLoot(serverLevel, source);
					createWitherRose(killer);
				}

				serverLevel.broadcastEntityEvent(this, (byte)3);
			}

			getCombatTracker().recheckStatus();
			setPose(Pose.DYING);
		}
	}

	@Override
	public void setInvulnerable(boolean isInvulnerable) {
		if (!INVULNERABLE.is(this, isInvulnerable))
			INVULNERABLE.setRaw(this, isInvulnerable);

		super.setInvulnerable(isInvulnerable);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);

		for (EntityDataHolder<?> dataHolder : this.dataParams) {
			if (dataHolder.checkSync(this, key))
				break;
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		compound.putBoolean("DropsLoot", this.hasDrops);

		if (getParts().length > 0) {
			List<Integer> disabledParts = new ObjectArrayList<>();

			for (int i = 0; i < getParts().length; i++) {
				if (!getParts()[i].isEnabled())
					disabledParts.add(i);
			}

			if (!disabledParts.isEmpty())
				compound.put("DisabledMultiparts", new IntArrayTag(disabledParts));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		this.hasDrops = compound.getBoolean("DropsLoot");

		if (compound.contains("DisabledMultiparts", Tag.TAG_INT_ARRAY)) {
			final AoAEntityPart<?>[] parts = getParts();

			for (int i : compound.getIntArray("DisabledMultiparts")) {
				parts[i].setEnabled(false);
			}
		}

		INVULNERABLE.set(this, isInvulnerable());
	}

	@Override
	public void aiStep() {
		super.aiStep();
		updateMultipartPositions();
	}

	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && this.hasDrops;
	}

	@Override
	public AoAEntityPart<?>[] getParts() {
		return this.parts;
	}

	@Override
	public boolean isMultipartEntity() {
		return isMultipartActive();
	}

	@Override
	public void refreshDimensions() {
		super.refreshDimensions();
		refreshMultipartDimensions();
	}

	@Override
	public void setParts(AoAEntityPart<?>... parts) {
		if (getParts().length > 0)
			throw new IllegalStateException("Cannot add more parts after having already done so!");

		defineParts(ENTITY_COUNTER, this::setId, this.parts = parts);
	}

	@Override
	public void setId(int id) {
		super.setId(id);
		setMultipartIds(id);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}
