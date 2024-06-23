package net.tslat.aoa3.content.entity.monster.unused;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import org.jetbrains.annotations.Nullable;


public class UriohEntity extends AoAMeleeMob<UriohEntity> {
	double lastHealth;

	public UriohEntity(EntityType<? extends UriohEntity> entityType, Level world) {
		super(entityType, world);

		lastHealth = getHealth();
	}

	@Override
	public float getEyeHeightAccess(Pose pose) {
		return getDimensions(pose).height() * 0.6f;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return AoASounds.ENTITY_APPARITION_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return AoASounds.ENTITY_APPARITION_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AoASounds.ENTITY_APPARITION_HURT.get();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (lastHealth != getHealth()) {
			refreshDimensions();
			lastHealth = getHealth();
		}
	}

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		final float scale = Math.max(0.1f, getHealth() / getMaxHealth());

		return super.getDefaultDimensions(pose).scale(0.5f * scale, 0.9375f * scale);
	}
}
