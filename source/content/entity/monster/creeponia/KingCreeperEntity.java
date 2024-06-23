package net.tslat.aoa3.content.entity.monster.creeponia;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import org.jetbrains.annotations.Nullable;


public class KingCreeperEntity extends AoACreeponiaCreeper {
    public KingCreeperEntity(EntityType<? extends AoACreeponiaCreeper> entityType, Level world) {
        super(entityType, world);

        fuseTime = 80;
    }

    @Override
    public float getEyeHeightAccess(Pose pose) {
        return 1.40625f;
    }

    @Override
	public float getExplosionStrength() {
        return 6f;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return AoASounds.ENTITY_CREEPOID_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AoASounds.ENTITY_CREEPOID_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AoASounds.ENTITY_CREEPOID_HURT.get();
    }
}
