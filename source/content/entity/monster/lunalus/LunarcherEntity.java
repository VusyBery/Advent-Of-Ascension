package net.tslat.aoa3.content.entity.monster.lunalus;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.aoa3.common.registration.AoAAttributes;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAWeapons;
import net.tslat.aoa3.content.entity.base.AoAFlyingRangedMob;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.content.entity.projectile.mob.BaseMobProjectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class LunarcherEntity extends AoAFlyingRangedMob {
	public LunarcherEntity(EntityType<? extends FlyingMob> entityType, Level world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
		SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);

		setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(AoAWeapons.LUNAR_BOW.get()));
		setDropChance(EquipmentSlot.MAINHAND, 0);

		return data;
	}

	@Override
	public float getEyeHeightAccess(Pose pose) {
		return 1.625f;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_LUNARCHER_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_LUNARCHER_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_LUNARCHER_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getShootSound() {
		return SoundEvents.ARROW_SHOOT;
	}

	@Override
	protected BaseMobProjectile getNewProjectileInstance() {
		return null;
	}

	@Override
	public void performRangedAttack(@NotNull LivingEntity target, float bowDamageFactor) {
		CustomArrowEntity projectile = new CustomArrowEntity(level(), AoAWeapons.LUNAR_BOW.get().getDefaultInstance(), this, getAttributeValue(AoAAttributes.RANGED_ATTACK_DAMAGE));

		double distanceFactorX = target.getX() - projectile.getX();
		double distanceFactorY = target.getBoundingBox().minY + (target.getBbHeight() / 3) - projectile.getY();
		double distanceFactorZ = target.getZ() - projectile.getZ();
		double hyp = Math.sqrt(distanceFactorX * distanceFactorX + distanceFactorZ * distanceFactorZ) * 0.05d;

		if (getShootSound() != null)
			playSound(getShootSound(), 1.0f, 1.0f);

		projectile.shoot(distanceFactorX, distanceFactorY + hyp, distanceFactorZ, 1.6f, (float)(4 - this.level().getDifficulty().getId()));
		level().addFreshEntity(projectile);
	}
}