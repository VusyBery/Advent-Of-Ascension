package net.tslat.aoa3.content.entity.monster.precasia;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import net.tslat.aoa3.library.object.EntityDataHolder;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class AttercopusEntity extends AoAMeleeMob<AttercopusEntity> {
	public static final EntityDataHolder<Boolean> CLIMBING = EntityDataHolder.register(AttercopusEntity.class, EntityDataSerializers.BOOLEAN, false, entity -> entity.climbing, (entity, value) -> entity.climbing = value);

	private boolean climbing = false;

	public AttercopusEntity(EntityType<? extends AttercopusEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);

		registerDataParams(builder, CLIMBING);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
	}

	@Override
	protected int getAttackSwingDuration() {
		return 10;
	}

	@Override
	protected int getPreAttackTime() {
		return 7;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SPIDER_DEATH;
	}

	@Override
	protected PathNavigation createNavigation(Level pLevel) {
		return new WallClimberNavigation(this, pLevel);
	}

	@Override
	public boolean onClimbable() {
		return CLIMBING.get(this);
	}

	@Override
	public void tick() {
		super.tick();

		if (!level().isClientSide)
			CLIMBING.set(this, this.horizontalCollision);
	}

	@Override
	public void makeStuckInBlock(BlockState state, Vec3 motionModifier) {
		if (!state.is(Blocks.COBWEB))
			super.makeStuckInBlock(state, motionModifier);
	}

	@Override
	protected DamageSource getAttackDamageSource(Entity target) {
		return target instanceof LivingEntity livingTarget ? damageSources().sting(livingTarget) : super.getAttackDamageSource(target);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
	}

	@Override
	protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
		return Math.max(0, super.calculateFallDamage(pFallDistance, pDamageMultiplier) - 10) / 2;
	}

	@Override
	protected void onAttack(Entity target) {
		if (target instanceof LivingEntity livingTarget)
			EntityUtil.applyPotions(livingTarget, new EffectBuilder(MobEffects.POISON, level().getDifficulty().getId() * 8 * 20));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(DefaultAnimations.genericWalkIdleController(this));
		controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_STRIKE));
	}
}
