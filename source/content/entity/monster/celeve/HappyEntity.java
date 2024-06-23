package net.tslat.aoa3.content.entity.monster.celeve;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAWeapons;
import net.tslat.aoa3.content.entity.base.AoARangedMob;
import net.tslat.aoa3.content.entity.projectile.mob.BaseMobProjectile;
import net.tslat.aoa3.content.entity.projectile.mob.ClownShotEntity;
import org.jetbrains.annotations.Nullable;


public class HappyEntity extends AoARangedMob<HappyEntity> {
	public HappyEntity(EntityType<? extends HappyEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
		SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);

		setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(AoAWeapons.CONFETTI_CANNON.get()));
		setDropChance(EquipmentSlot.MAINHAND, 0);

		return data;
	}

	@Override
	public float getEyeHeightAccess(Pose pose) {
		return 1.8125f;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_CELEVE_CLOWN_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_CELEVE_CLOWN_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_CELEVE_CLOWN_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getShootSound() {
		return AoASounds.ENTITY_HAPPY_SHOOT.get();
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean killedByPlayer) {}

	@Override
	protected BaseMobProjectile getNewProjectileInstance() {
		return new ClownShotEntity(this, BaseMobProjectile.Type.MAGIC);
	}
}
