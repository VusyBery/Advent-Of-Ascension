package net.tslat.aoa3.content.entity.boss.tyrosaur;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.block.AoAFluidTypes;
import net.tslat.aoa3.common.registration.entity.AoAMonsters;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.EntitySpawningUtil;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class WoundedTyrosaurEntity extends AoAMeleeMob<WoundedTyrosaurEntity> {
    public WoundedTyrosaurEntity(EntityType<? extends WoundedTyrosaurEntity> entityType, Level level) {
        super(entityType, level);

        setParts(new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, -getBbWidth() * 0.5f - 7 / 16f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.4375f, -getBbWidth() * 1.35f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.375f, -getBbWidth() * 1.75f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, getBbWidth() * 0.5f + 7 / 16f),
                new AoAEntityPart<>(this, 11/ 16f, 1, 0, 0.5f, getBbWidth() * 1.4f));
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AoASounds.ENTITY_TYROSAUR_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AoASounds.ENTITY_TYROSAUR_HURT.get();
    }

    @Override
    protected float getStepWeight() {
        return 3f;
    }

    @Override
    protected boolean isQuadruped() {
        return true;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);

        setHealth((float)RandomUtil.randomValueBetween(0.15f, 0.2f) * getMaxHealth());

        return spawnGroupData;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (getKillCredit() instanceof ServerPlayer pl)
            InventoryUtil.findItem(pl, stack -> stack.is(AoAItems.BONE_HORN) && stack.isDamaged() && stack.getDamageValue() == 1).ifPresent(pair -> pair.right().setDamageValue(0));
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        if (level() instanceof ServerLevel level && damageContainer.getSource().is(DamageTypeTags.IS_FIRE) && level().getFluidState(BlockPos.containing(getEyePosition())).getFluidType() == AoAFluidTypes.TAR.get() && level().getFluidState(blockPosition().above()).getFluidType() == AoAFluidTypes.TAR.get()) {
            ParticleBuilder.forRandomPosInEntity(ParticleTypes.LARGE_SMOKE, this)
                    .colourOverride(0xFFFFFF)
                    .spawnNTimes(20)
                    .sendToAllPlayersTrackingEntity(level, this);

            if (isDeadOrDying()) {
                AoAScheduler.scheduleSyncronisedTask(() -> {
                    EntitySpawningUtil.spawnEntity(level, AoAMonsters.SKELETRON.get(), position(), MobSpawnType.CONVERSION, abomination -> {
                        abomination.setXRot(getXRot());
                        abomination.setYRot(getYRot());
                        abomination.setYHeadRot(getYHeadRot());
                    });
                }, 19 - this.deathTime);
            }
        }
    }

    @Override
    protected int getPreAttackTime() {
        return 5;
    }

    @Override
    public int getCurrentSwingDuration() {
        return 11;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkIdleController(this));
        controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_BITE).transitionLength(0));
    }
}
