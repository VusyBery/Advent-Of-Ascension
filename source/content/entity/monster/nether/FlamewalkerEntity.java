package net.tslat.aoa3.content.entity.monster.nether;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.fluids.FluidType;
import net.tslat.aoa3.client.render.AoAAnimations;
import net.tslat.aoa3.common.particleoption.EntityTrackingParticleOptions;
import net.tslat.aoa3.common.registration.AoAAttributes;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.entity.AoADamageTypes;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.common.registration.entity.AoAMobEffects;
import net.tslat.aoa3.content.entity.base.AoARangedMob;
import net.tslat.aoa3.content.entity.projectile.mob.BaseMobProjectile;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.effectslib.networking.packet.TELParticlePacket;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StayWithinDistanceOfAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;


public class FlamewalkerEntity extends AoARangedMob<FlamewalkerEntity> {
    public FlamewalkerEntity(EntityType<? extends FlamewalkerEntity> entityType, Level world) {
        super(entityType, world);

        setPathfindingMalus(PathType.LAVA, 4);
        setPathfindingMalus(PathType.DANGER_FIRE, 0);
        setPathfindingMalus(PathType.DAMAGE_FIRE, 0);
        setPathfindingMalus(PathType.WATER_BORDER, 8);
        setPathfindingMalus(PathType.WATER, -1);
    }

    @Override
    public BrainActivityGroup<FlamewalkerEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new StayWithinDistanceOfAttackTarget<>(),
                new FlameWalkerAttack(getPreAttackTime()).attackInterval(entity -> getAttackSwingDuration()));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return AoASounds.ENTITY_FLAMEWALKER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return AoASounds.ENTITY_FLAMEWALKER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return AoASounds.ENTITY_FLAMEWALKER_HURT.get();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getShootSound() {
        return null;
    }

    @Override
    public boolean canSwimInFluidType(FluidType type) {
        return type == NeoForgeMod.LAVA_TYPE.value();
    }

    protected int getAttackSwingDuration() {
        return 32;
    }

    protected int getPreAttackTime() {
        return 16;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {}

    @Override
    protected BaseMobProjectile getNewProjectileInstance() {
        return null;
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        if (DamageUtil.isMeleeDamage(damageContainer.getSource()) && damageContainer.getSource().getEntity() instanceof LivingEntity attacker) {
            attacker.igniteForSeconds(3);

            if (DamageUtil.safelyDealDamage(DamageUtil.entityDamage(AoADamageTypes.MOB_FIRE_RECOIL, this), attacker, 3) && rand().oneInNChance(15))
                EntityUtil.applyPotions(attacker, new EffectBuilder(AoAMobEffects.BURNED, 600));
        }
    }

    @Override
    public void doRangedAttackEntity(@org.jetbrains.annotations.Nullable BaseMobProjectile projectile, Entity target) {
        if (target.getRemainingFireTicks() < 200)
            target.igniteForSeconds((int)Math.ceil(Math.max(0, target.getRemainingFireTicks()) / 20f) + 1);

       if (DamageUtil.safelyDealDamage(DamageUtil.positionedEntityDamage(AoADamageTypes.MOB_FLAMETHROWER, this, position()), target, (float)getAttributeValue(AoAAttributes.RANGED_ATTACK_DAMAGE)) && rand().oneInNChance(10))
           EntityUtil.applyPotions(target, new EffectBuilder(AoAMobEffects.BURNED, 600));
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public static SpawnPlacements.SpawnPredicate<Mob> spawnRules() {
        return AoAEntitySpawnPlacements.SpawnBuilder.DEFAULT.noPeacefulSpawn().noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock();
    }

    public static AoAEntityStats.AttributeBuilder entityStats(EntityType<FlamewalkerEntity> entityType) {
        return AoAEntityStats.AttributeBuilder.createMonster(entityType)
                .health(45)
                .moveSpeed(0.3)
                .projectileDamage(7)
                .followRange(10)
                .aggroRange(16);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericLivingController(this),
                DefaultAnimations.genericWalkIdleController(this),
                AoAAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_STRIKE));
    }

    private static class FlameWalkerAttack extends AnimatableRangedAttack<FlamewalkerEntity> {
        private Vec3 targetingPosition = null;

        public FlameWalkerAttack(int delayTicks) {
            super(delayTicks);
        }

        @Override
        protected void start(FlamewalkerEntity entity) {
            super.start(entity);

            this.targetingPosition = this.target.position().add(this.target.getBbWidth() * 0.5f, 0, this.target.getBbWidth() * 0.5f);

            ParticleBuilder.forPositions(ParticleTypes.SMALL_FLAME,
                    () -> new Vec3(targetingPosition.x + entity.rand().randomValueBetween(-1, 1f), targetingPosition.y + 0.1f, targetingPosition.z + entity.rand().randomValueBetween(-1, 1)), 10)
                    .sendToAllPlayersTrackingEntity((ServerLevel)entity.level(), entity);

            entity.level().playSound(null, this.targetingPosition.x, this.targetingPosition.y, this.targetingPosition.z, AoASounds.ENTITY_FLAMEWALKER_FLARE.get(), SoundSource.HOSTILE, 1, 1);
        }

        @Override
        protected void doDelayedAction(FlamewalkerEntity entity) {
            if (this.target == null)
                return;

            if (!BrainUtils.canSee(entity, this.target) || entity.distanceToSqr(this.target) > this.attackRadius)
                return;

            TELParticlePacket packet = new TELParticlePacket(15);

            for (int i = 0; i < 15; i++) {
                packet.particle(ParticleBuilder.forPositions(EntityTrackingParticleOptions.fromEntity(AoAParticleTypes.BURNING_FLAME, entity), this.targetingPosition.add(entity.rand().randomValueBetween(-1, 1f), 0, entity.rand().randomValueBetween(-1, 1)))
                        .scaleMod(0.5f)
                        .lifespan(Mth.ceil(2 / (entity.random.nextFloat() * 0.8f + 0.2f)))
                        .velocity(entity.rand().randomValueBetween(-0.05f, 0.05f), 0.5, entity.rand().randomValueBetween(-0.05f, 0.05f)));
            }

            packet.sendToAllPlayersTrackingEntity((ServerLevel)entity.level(), entity);
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));
        }

        @Override
        protected void stop(FlamewalkerEntity entity) {
            super.stop(entity);

            this.targetingPosition = null;
        }
    }
}
