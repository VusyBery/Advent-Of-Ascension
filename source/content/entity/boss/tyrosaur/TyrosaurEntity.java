package net.tslat.aoa3.content.entity.boss.tyrosaur;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.aoa3.common.registration.block.AoAFluidTypes;
import net.tslat.aoa3.common.registration.entity.AoAMonsters;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.boss.AoABoss;
import net.tslat.aoa3.library.object.EntityDataHolder;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.EntitySpawningUtil;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class TyrosaurEntity extends AoABoss {
    public static final EntityDataHolder<Boolean> WOUNDED = EntityDataHolder.register(TyrosaurEntity.class, EntityDataSerializers.BOOLEAN, false, entity -> entity.wounded, (entity, value) -> entity.wounded = value);

    private boolean wounded = false;

    public TyrosaurEntity(EntityType<? extends TyrosaurEntity> entityType, Level level) {
        super(entityType, level);

        setParts(new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, -getBbWidth() * 0.5f - 7 / 16f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.4375f, -getBbWidth() * 1.35f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.375f, -getBbWidth() * 1.75f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, getBbWidth() * 0.5f + 7 / 16f),
                new AoAEntityPart<>(this, 11/ 16f, 1, 0, 0.5f, getBbWidth() * 1.4f));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        registerDataParams(builder, WOUNDED);
    }

    @Override
    public @Nullable SoundEvent getMusic() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("Wounded", WOUNDED.get(this));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Wounded", CompoundTag.TAG_BYTE))
            WOUNDED.set(this, compound.getBoolean("Wounded"));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (getKillCredit() instanceof ServerPlayer pl && WOUNDED.is(this, true))
            InventoryUtil.findItem(pl, stack -> stack.is(AoAItems.BONE_HORN) && stack.isDamaged()).ifPresent(pair -> pair.right().setDamageValue(0));
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        if (level() instanceof ServerLevel level && damageContainer.getSource().is(DamageTypeTags.IS_FIRE) && WOUNDED.is(this, true) && level().getFluidState(BlockPos.containing(getEyePosition())).getFluidType() == AoAFluidTypes.TAR.get() && level().getFluidState(blockPosition().above()).getFluidType() == AoAFluidTypes.TAR.get()) {
            ParticleBuilder.forRandomPosInEntity(ParticleTypes.LARGE_SMOKE, this)
                    .colourOverride(255, 255, 255, 255)
                    .spawnNTimes(20)
                    .sendToAllPlayersTrackingEntity(level,this);

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 0, state -> {
            if (state.isMoving()) {
                if (isSprinting())
                    return state.setAndContinue(DefaultAnimations.RUN);

                return state.setAndContinue(WOUNDED.is(this, true) ? RawAnimation.begin().thenLoop("move.walk.wounded") : DefaultAnimations.WALK);
            }

            return state.setAndContinue(WOUNDED.is(this, true) ? RawAnimation.begin().thenLoop("misc.idle.wounded") : DefaultAnimations.IDLE);
        }));
    }
}
