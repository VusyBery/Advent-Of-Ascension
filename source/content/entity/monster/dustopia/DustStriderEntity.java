package net.tslat.aoa3.content.entity.monster.dustopia;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import org.jetbrains.annotations.Nullable;


public class DustStriderEntity extends AoAMeleeMob<DustStriderEntity> {
    public DustStriderEntity(EntityType<? extends DustStriderEntity> entityType, Level world) {
        super(entityType, world);
    }

    /*public DustStriderEntity(DustonEntity duston) {
        this(AoAMonsters.DUST_STRIDER.get(), duston.level());

        moveTo(duston.getX(), duston.getY() + 0.5, duston.getZ(), duston.getYRot(), duston.getXRot());
        fallDistance = -10;
    }*/

    @Override
    public float getEyeHeightAccess(Pose pose) {
        return 0.8125f;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return AoASounds.ENTITY_DUST_STRIDER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AoASounds.ENTITY_DUST_STRIDER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AoASounds.ENTITY_DUST_STRIDER_HURT.get();
    }
}
