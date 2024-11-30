package net.tslat.aoa3.content.entity.boss.tyrosaur;

import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.ScreenShakePacket;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.entity.AoADamageTypes;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.common.registration.entity.AoAMiscEntities;
import net.tslat.aoa3.common.registration.entity.AoAMobEffects;
import net.tslat.aoa3.content.entity.ai.mob.AnimatableMeleeAttack;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.boss.AoABoss;
import net.tslat.aoa3.content.entity.boss.ArmouredBoss;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyLivingEntitySensor;
import net.tslat.aoa3.content.entity.brain.sensor.AggroBasedNearbyPlayersSensor;
import net.tslat.aoa3.content.entity.misc.EarthquakeBlockEntity;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.integration.tes.TESIntegration;
import net.tslat.aoa3.library.builder.SoundBuilder;
import net.tslat.aoa3.library.object.EntityDataHolder;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.MathUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.effectslib.api.particle.transitionworker.AwayFromPositionParticleTransition;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.effectslib.networking.packet.TELParticlePacket;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.HeldBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EliteTyrosaurEntity extends AoABoss implements ArmouredBoss {
    private static final float ARMOUR_MODIFIER = 60;
    private static final float MAX_ARMOUR = 600f;
    public static final EntityDataHolder<Float> ARMOUR = EntityDataHolder.register(EliteTyrosaurEntity.class, EntityDataSerializers.FLOAT, MAX_ARMOUR, tyrosaur -> tyrosaur.armour, (tyrosaur, value) -> tyrosaur.armour = value);
    private static final RawAnimation ROAR_START_ANIM = RawAnimation.begin().thenPlay("misc.roar.start");
    private static final RawAnimation ROAR_ANIM = RawAnimation.begin().thenPlay("misc.roar.hold");
    private static final RawAnimation ROAR_STOP_ANIM = RawAnimation.begin().thenPlay("misc.roar.stop");
    private static final RawAnimation BRACE_START_ANIM = RawAnimation.begin().thenPlay("misc.brace.start");
    private static final RawAnimation BRACE_ANIM = RawAnimation.begin().thenPlay("misc.brace.hold");
    private static final RawAnimation BRACE_STOP_ANIM = RawAnimation.begin().thenPlay("misc.brace.stop");
    private static final int BITE = 0;
    private static final int HORN = 1;
    private static final int SLAM = 2;
    private static final int ROAR = 3;
    private static final int BRACE = 4;

    private float armour = MAX_ARMOUR;

    public EliteTyrosaurEntity(EntityType<? extends EliteTyrosaurEntity> entityType, Level level) {
        super(entityType, level);

        setParts(new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, -getBbWidth() * 0.5f - 7 / 16f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.4375f, -getBbWidth() * 1.35f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 9 / 16f, 0.5625f, 0, 0.375f, -getBbWidth() * 1.75f).setDamageMultiplier(0.85f),
                new AoAEntityPart<>(this, 14 / 16f, getBbHeight() - 0.625f, 0, 0.5f, getBbWidth() * 0.5f + 7 / 16f),
                new AoAEntityPart<>(this, 11/ 16f, 1, 0, 0.5f, getBbWidth() * 1.4f),
                new AoAEntityPart<>(this, 1f, getBbHeight() - 0.75f, 0, getBbHeight() - 0.25f, getBbWidth() * 0.5f + 2 / 16f) {
                    @Override
                    public boolean hurt(DamageSource source, float amount) {
                        if (!DamageUtil.isMeleeDamage(source))
                            return false;

                        return super.hurt(source, amount);
                    }

                    @Override
                    public ProjectileDeflection deflection(Projectile projectile) {
                        return (projectile2, entity, random) -> {
                            Vec3 vec3 = projectile2.getDeltaMovement().cross(MathUtil.getEyelineForward(EliteTyrosaurEntity.this)).add(0, 0.4f, 0).normalize();
                            projectile2.setDeltaMovement(vec3);
                            projectile2.hasImpulse = true;
                        };
                    }
                });
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        registerDataParams(builder, ARMOUR);
    }

    @Override
    protected void addSwingData(SwingData swings) {
        swings.put(BITE, new SwingData.Swing(11, 5, DefaultAnimations.ATTACK_BITE));
        swings.put(HORN, new SwingData.Swing(13, 6, DefaultAnimations.ATTACK_THROW));
    }

    @Nullable
    @Override
    public SoundEvent getMusic() {
        return AoASounds.TYROSAUR_MUSIC.get();
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
        return 4f;
    }

    @Override
    protected boolean isQuadruped() {
        return true;
    }

    @Override
    protected int getPreAttackTime() {
        return getSwingWarmupTicks();
    }

    @Override
    protected int getAttackSwingDuration() {
        return getSwingDurationTicks();
    }

    @Override
    public float getArmourPercent() {
        return Mth.clamp(ARMOUR.get(this) / MAX_ARMOUR, 0, 1);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        updateArmour(getArmourPercent() * MAX_ARMOUR);

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public List<ExtendedSensor<? extends AoABoss>> getSensors() {
        return ObjectArrayList.of(
                new AggroBasedNearbyPlayersSensor<>(),
                new AggroBasedNearbyLivingEntitySensor<AoABoss>().setPredicate((target, entity) -> target instanceof OwnableEntity tamedEntity && tamedEntity.getOwnerUUID() != null).setScanRate(entity -> 40),
                new HurtBySensor<>(),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends AoABoss> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> !DamageUtil.isAttackable(target) || distanceToSqr(target.position()) > Math.pow(getAttributeValue(Attributes.FOLLOW_RANGE), 2)),
                new SetWalkTargetToAttackTarget<>().speedMod((entity, target) -> 1.25f),
                new FirstApplicableBehaviour<>(
                        new ReactToUnreachableTarget<>()
                                .timeBeforeReacting(entity -> 80)
                                .reaction((entity, isTowering) -> BrainUtils.clearMemory(entity, SBLMemoryTypes.SPECIAL_ATTACK_COOLDOWN.get()))
                                .cooldownFor(entity -> 80),
                        new OneRandomBehaviour<>(
                                Pair.of(new AnimatableMeleeAttack<>(getSwingWarmupTicks(BITE))
                                        .attackInterval(entity -> getSwingDurationTicks(BITE) + 2)
                                        .whenStarting(entity -> ATTACK_STATE.set(entity, BITE)), 30),
                                Pair.of(new AnimatableMeleeAttack<>(getSwingWarmupTicks(HORN))
                                        .attackEffect((entity, target) -> {
                                            target.setDeltaMovement(MathUtil.getEyelineForward(entity).scale(-0.75f).add(0, 1.5f, 0));
                                            EntityUtil.applyPotions(target, new EffectBuilder(AoAMobEffects.BLEEDING, 200));
                                        })
                                        .attackInterval(entity -> getSwingDurationTicks(HORN) + 2)
                                        .whenStarting(entity -> ATTACK_STATE.set(entity, HORN)), 5),
                                Pair.of(new EliteTyrosaurEntity.Roar()
                                        .startCondition(entity -> !BrainUtils.isOnSpecialCooldown(entity))
                                        .runFor(entity -> BrainUtils.hasMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE) ? entity.random.nextInt(160, 200) : entity.random.nextInt(100, 200))
                                        .cooldownFor(entity -> BrainUtils.hasMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE) ? entity.random.nextInt(80, 140) : entity.random.nextInt(400, 800))
                                        .whenStopping(entity -> BrainUtils.setSpecialCooldown(entity, 150)), 1),
                                Pair.of(new EliteTyrosaurEntity.Earthquake()
                                        .startCondition(entity -> !BrainUtils.isOnSpecialCooldown(entity) && !BrainUtils.hasMemory(entity, SBLMemoryTypes.TARGET_UNREACHABLE.get()))
                                        .cooldownFor(entity -> entity.random.nextInt(200, 400))
                                        .whenStopping(entity -> BrainUtils.setSpecialCooldown(entity, 100)), 3))
                ));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (ARMOUR.get(this) > 0 && !level().isClientSide && !DamageUtil.isEnvironmentalDamage(source)) {
            float armourDamage = amount;

            if (source.is(DamageTypeTags.IS_PROJECTILE) && !DamageUtil.isMagicDamage(source) && !source.is(AoADamageTypes.HEAVY_GUN))
                armourDamage *= 0.25f;

            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                if (DamageUtil.isMeleeDamage(source) && source.getDirectEntity() instanceof LivingEntity attacker) {
                    ItemStack weapon = attacker.getMainHandItem();

                    if (weapon.canPerformAction(ItemAbilities.PICKAXE_DIG)) {
                        armourDamage *= 1 + (weapon.getDestroySpeed(Blocks.STONE.defaultBlockState()) * 0.075f) + (float)attacker.getAttributeValue(Attributes.MINING_EFFICIENCY) * 0.04f;
                    }
                    else {
                        armourDamage *= 0.5f;
                    }
                }
                else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    armourDamage *= 1.25f;
                }

                if (IntegrationManager.isTESActive())
                    TESIntegration.sendParticle(this, armourDamage, 0xFFAAAAAA);

                updateArmour(ARMOUR.get(this) - armourDamage);
            }
        }

        return super.hurt(source, amount);
    }

    private void updateArmour(float newArmour) {
        newArmour = Mth.clamp(newArmour, 0, MAX_ARMOUR);
        int oldArmourStage = Mth.ceil(ARMOUR.get(this) / MAX_ARMOUR * 4);
        int newArmourStage = Mth.ceil(newArmour / MAX_ARMOUR * 4);

        ARMOUR.set(this, newArmour);

        float armourPercent = getArmourPercent();

        AttributeUtil.applyTransientModifier(this, Attributes.ARMOR, getArmourMod(armourPercent));
        AttributeUtil.applyTransientModifier(this, Attributes.ARMOR_TOUGHNESS, getArmourToughnessMod(armourPercent));

        if (oldArmourStage != newArmourStage) {
            new SoundBuilder(AoASounds.STONE_CRUMBLE)
                    .followEntity(this)
                    .execute();
            ParticleBuilder.forRandomPosInEntity(ParticleTypes.DUST_PLUME, this)
                    .lifespan(20)
                    .colourOverride(0.5f, 0.5f, 0.5f, 1f)
                    .spawnNTimes(20)
                    .addTransition(AwayFromPositionParticleTransition.create(position().add(0, getBbHeight() * 0.5f, 0), 10))
                    .sendToAllPlayersTrackingEntity((ServerLevel)level(), this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putFloat("ArmourPercent", getArmourPercent());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("ArmourPercent", Tag.TAG_FLOAT))
            updateArmour(compound.getFloat("ArmourPercent") * MAX_ARMOUR);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        AABB boundingBox = getBoundingBox();

        if (getVehicle() != null) {
            AABB vehicleBounds = getVehicle().getBoundingBox();
            boundingBox = new AABB(
                    Math.min(boundingBox.minX, vehicleBounds.minX),
                    boundingBox.minY,
                    Math.min(boundingBox.minZ, vehicleBounds.minZ),
                    Math.max(boundingBox.maxX, vehicleBounds.maxX),
                    boundingBox.maxY,
                    Math.max(boundingBox.maxZ, vehicleBounds.maxZ)
            );
        }

        return boundingBox.inflate(1.65f, 0, 1.65f);
    }

    public static AoAEntityStats.AttributeBuilder entityStats(EntityType<EliteTyrosaurEntity> entityType) {
        return AoAEntityStats.AttributeBuilder.createMonster(entityType)
                .health(635)
                .moveSpeed(0.2875f)
                .meleeStrength(15)
                .knockbackResist(0.9)
                .followRange(100)
                .aggroRange(64)
                .armour(10, 15)
                .knockback(1f)
                .stepHeight(1.25f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Main", 0, state -> {
            if (ATTACK_STATE.is(this, ROAR))
                return state.setAndContinue(ROAR_ANIM);

            if (state.isMoving())
                return state.setAndContinue(isSprinting() ? DefaultAnimations.RUN : DefaultAnimations.WALK);

            return state.setAndContinue(DefaultAnimations.IDLE);
        })
                .triggerableAnim("roar_start", ROAR_START_ANIM)
                .triggerableAnim("roar_stop", ROAR_STOP_ANIM)
                .triggerableAnim("slam", DefaultAnimations.ATTACK_SLAM));
        controllers.add(new AnimationController<GeoAnimatable>(this, "Attack", 0, state -> {
            if (this.swinging)
                return state.setAndContinue(getSwingAnimation());

            state.resetCurrentAnimation();

            return PlayState.STOP;
        }));
    }


    private static AttributeModifier getArmourMod(float percent) {
        return new AttributeModifier(AdventOfAscension.id("tyrosaur_armour"), ARMOUR_MODIFIER * percent, AttributeModifier.Operation.ADD_VALUE);
    }

    private static AttributeModifier getArmourToughnessMod(float percent) {
        return new AttributeModifier(AdventOfAscension.id("tyrosaur_armour_toughness"), ARMOUR_MODIFIER * percent, AttributeModifier.Operation.ADD_VALUE);
    }

    private static class Earthquake extends DelayedBehaviour<EliteTyrosaurEntity> {
        private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

        public Earthquake() {
            super(25);
        }

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return MEMORY_REQUIREMENTS;
        }

        @Override
        protected void start(EliteTyrosaurEntity entity) {
            ATTACK_STATE.set(entity, SLAM);
            entity.triggerAnim("Main", "slam");
            entity.setImmobile(true);
        }

        @Override
        protected void doDelayedAction(EliteTyrosaurEntity entity) {
            ServerLevel level = (ServerLevel)entity.level();
            Vec3 stompPos = entity.position().add(MathUtil.getBodyForward(entity).scale(1.35f));

            doFX(entity, stompPos);

            Vec3 earthquakeOrigin = stompPos.subtract(0, 1, 0);

            AoAScheduler.scheduleSyncronisedTask(() -> {
                for (ServerPlayer player : level.players()) {
                    if (player.distanceToSqr(entity) > 20 * 20)
                        continue;

                    AoANetworking.sendToPlayer(player, new ScreenShakePacket(10, Math.max(0.1f, (1 - (float)earthquakeOrigin.distanceToSqr(player.position()) / (20f * 20f)) * 2), 0.98f));
                }
            }, 3);

            for (int i = 0; i < 20; i++) {
                final int tick = i;

                AoAScheduler.scheduleSyncronisedTask(() -> {
                    Set<Pair<BlockPos, BlockState>> positions;

                    try (BulkSectionAccess access = new BulkSectionAccess(level)) {
                        positions = Streams.stream(MathUtil.inLateralCircle(earthquakeOrigin, tick, 64))
                                .unordered()
                                .map(BlockPos::containing)
                                .distinct()
                                .map(pos -> {
                                    BlockState state = access.getBlockState(pos);

                                    if (!state.blocksMotion()) {
                                        int offset = 10;

                                        while (offset-- > 0 && !(state = access.getBlockState(pos = pos.below())).blocksMotion()) {}
                                    }
                                    else if (access.getBlockState(pos.above()).blocksMotion()) {
                                        int offset = 10;

                                        while (offset-- > 0 && access.getBlockState(pos = pos.above()).blocksMotion()) {}

                                        state = access.getBlockState(pos = pos.below());
                                    }

                                    return Pair.of(pos, state);
                                })
                                .filter(pair -> pair.getSecond().blocksMotion())
                                .filter(pair -> !access.getBlockState(pair.getFirst().above()).blocksMotion())
                                .collect(Collectors.toSet());
                    }

                    TELParticlePacket packet = new TELParticlePacket();

                    if (entity.tickCount % 3 == 0) {
                        for (ServerPlayer player : level.players()) {
                            if (player.distanceToSqr(entity) > 32 * 32)
                                continue;

                            Vec3 velocity = earthquakeOrigin.vectorTo(player.position()).normalize();

                            new SoundBuilder(AoASounds.FX_RUBBLE)
                                    .isBlocks()
                                    .atPos(level, earthquakeOrigin.add(velocity.scale(tick)))
                                    .moving(velocity)
                                    .pitch(0.75f)
                                    .category(SoundSource.BLOCKS)
                                    .include(player)
                                    .execute();
                        }
                    }

                    for (Pair<BlockPos, BlockState> ringPos : positions) {
                        EarthquakeBlockEntity block = new EarthquakeBlockEntity(AoAMiscEntities.EARTHQUAKE_BLOCK.get(), level, ringPos.getSecond(), ringPos.getFirst(), entity);

                        block.setDamage(8);
                        block.setDeltaMovement(new Vec3(0, 0.35f, 0));
                        block.setPos(Vec3.atCenterOf(ringPos.getFirst()));
                        level.addFreshEntity(block);
                        packet.particle(ParticleBuilder.forRandomPosInBlock(new BlockParticleOption(ParticleTypes.BLOCK, ringPos.getSecond()), ringPos.getFirst())
                                .lifespan(entity.random.nextInt(20, 40))
                                .scaleMod(0.5f)
                                .velocity(entity.random.nextGaussian() * 0.1f, entity.random.nextFloat() * 0.2f + 0.2f, entity.random.nextGaussian() * 0.1f));
                    }

                    packet.sendToAllPlayersTrackingEntity(level, entity);
                }, i + 1);
            }
        }

        private void doFX(EliteTyrosaurEntity entity, Vec3 stompPos) {
            Vec3 right = MathUtil.getBodyRight(entity);
            Vec3 leftFootPos = stompPos.add(right.scale(0.75f));
            Vec3 rightFootPos = stompPos.add(right.scale(-0.75f));

            BlockState leftFootState = entity.level().getBlockState(BlockPos.containing(leftFootPos.x, leftFootPos.y - 0.1f, leftFootPos.z));
            BlockState rightFootState = entity.level().getBlockState(BlockPos.containing(rightFootPos.x, rightFootPos.y - 0.1f, rightFootPos.z));
            new SoundBuilder(AoASounds.FX_SLAM)
                    .atPos(entity.level(), stompPos)
                    .isMonster()
                    .radius(32)
                    .execute();
            TELParticlePacket packet = new TELParticlePacket();

            if (!leftFootState.isAir()) {
                for (int i = 0; i < 20; i++) {
                    packet.particle(ParticleBuilder.forPositions(new BlockParticleOption(ParticleTypes.BLOCK, leftFootState), leftFootPos)
                            .lifespan(entity.random.nextInt(40, 60))
                            .velocity(entity.random.nextGaussian() * 0.1f, entity.random.nextFloat() * 0.3f + 0.2f, entity.random.nextGaussian() * 0.1f));

                    if (i < 5) {
                        packet.particle(ParticleBuilder.forPositions(ParticleTypes.CAMPFIRE_COSY_SMOKE, leftFootPos)
                                .colourOverride(1f, 1f, 1f, 0.5f)
                                .lifespan(entity.random.nextInt(40, 60))
                                .velocity(entity.random.nextGaussian() * 0.05f, entity.random.nextFloat() * 0.1, entity.random.nextGaussian() * 0.05f));
                    }
                }
            }

            if (!rightFootState.isAir()) {
                for (int i = 0; i < 10; i++) {
                    packet.particle(ParticleBuilder.forPositions(new BlockParticleOption(ParticleTypes.BLOCK, rightFootState), rightFootPos)
                            .lifespan(entity.random.nextInt(40, 60))
                            .velocity(entity.random.nextGaussian() * 0.1f, entity.random.nextFloat() * 0.3f + 0.2f, entity.random.nextGaussian() * 0.1f));

                    if (i < 5) {
                        packet.particle(ParticleBuilder.forPositions(ParticleTypes.CAMPFIRE_COSY_SMOKE, rightFootPos)
                                .colourOverride(1f, 1f, 1f, 0.5f)
                                .lifespan(entity.random.nextInt(40, 60))
                                .velocity(entity.random.nextGaussian() * 0.05f, entity.random.nextFloat() * 0.1, entity.random.nextGaussian() * 0.05f));
                    }
                }
            }

            packet.sendToAllPlayersTrackingEntity((ServerLevel)entity.level(), entity);
        }

        @Override
        protected void stop(EliteTyrosaurEntity entity) {
            ATTACK_STATE.set(entity, BITE);
            entity.setImmobile(false);
        }
    }

    private static class Roar extends HeldBehaviour<EliteTyrosaurEntity> {
        private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

        private int roarSoundDelay = 0;

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return MEMORY_REQUIREMENTS;
        }

        @Override
        protected void start(EliteTyrosaurEntity entity) {
            new SoundBuilder(AoASounds.ENTITY_TYROSAUR_ROAR_START).followEntity(entity.getParts()[4]).execute();
            this.roarSoundDelay = entity.random.nextInt(4, 8);
            ATTACK_STATE.set(entity, ROAR);
            entity.setImmobile(true);
            entity.triggerAnim("Main", "roar_start");
        }

        @Override
        protected boolean shouldKeepRunning(EliteTyrosaurEntity entity) {
            return BrainUtils.getTargetOfEntity(entity) != null;
        }

        @Override
        protected void tick(EliteTyrosaurEntity entity) {
            if (this.runningTime > 30 && this.runningTime % this.roarSoundDelay == 0) {
                this.roarSoundDelay = entity.random.nextInt(5, 7);
                new SoundBuilder(AoASounds.ENTITY_TYROSAUR_ROAR_LOOP).followEntity(entity.getParts()[4]).execute();
            }

            if (this.runningTime > 33 && entity.tickCount % 2 == 0) {
                ServerLevel level = (ServerLevel)entity.level();
                Vec3 forward = MathUtil.getBodyForward(entity);
                Vec3 pos = entity.getEyePosition().add(forward.scale(entity.getBbWidth() * 1.4f - 0.3f));
                TELParticlePacket packet = new TELParticlePacket();

                if (entity.tickCount % 6 == 0) {
                    packet.particle(ParticleBuilder.forPositionsInCircle(AoAParticleTypes.ORB.get(), pos, forward, 0.25f, 32)
                            .lifespan(20)
                            .scaleMod(0.4f)
                            .colourOverride(1, 1, 1, 0.5f)
                            .addTransition(AwayFromPositionParticleTransition.create(pos.subtract(forward.scale(0.25f)), 3)));
                }
                else if (entity.tickCount % 8 == 0) {
                    packet.particle(ParticleBuilder.forPositionsInCircle(AoAParticleTypes.ORB.get(), pos, forward, 0.25f, 32)
                            .lifespan(20)
                            .scaleMod(0.4f)
                            .colourOverride(1, 1, 1, 0.5f)
                            .addTransition(AwayFromPositionParticleTransition.create(pos.subtract(forward.scale(0.1f)), 3)));
                }
                else {
                    packet.particle(ParticleBuilder.forRandomPosInSphere(ParticleTypes.DUST_PLUME, entity.getEyePosition(), 0.05f)
                            .lifespan(20)
                            .spawnNTimes(20)
                            .addTransition(AwayFromPositionParticleTransition.create(entity.getEyePosition(), 10)));
                }

                packet.sendToAllPlayersTrackingEntity(level, entity);

                if (entity.tickCount % 10 == 0) {
                    for (LivingEntity target : EntityRetrievalUtil.<LivingEntity>getEntities(level, entity.getBoundingBox().inflate(32), target -> target instanceof LivingEntity && target != entity)) {
                        float strength = Math.max(0.2f, 1 - (float)entity.distanceToSqr(target) / 300f);
                        float hearingMod = (float)(target.getViewVector(1).normalize().dot(target.getEyePosition().vectorTo(entity.getEyePosition()).normalize()) + 1) * 0.5f;

                        if (target.isCrouching())
                            hearingMod -= 0.25f;

                        strength *= 1 + hearingMod;

                        if (DamageUtil.doMiscEnergyAttack(entity, target, strength * 2f, entity.position())) {
                            if (hearingMod > 0.5f) {
                                EntityUtil.applyPotions(target, new EffectBuilder(MobEffects.DIG_SLOWDOWN, 200).isAmbient());

                                if (hearingMod > 0.8f && !target.hasEffect(MobEffects.WEAKNESS))
                                    EntityUtil.applyPotions(target, new EffectBuilder(MobEffects.WEAKNESS, 200).isAmbient());
                            }

                            if (target instanceof ServerPlayer pl)
                                AoANetworking.sendToPlayer(pl, new ScreenShakePacket(20, strength * 1.5f, 0.98f));
                        }
                    }

                }
            }
        }

        @Override
        protected void stop(EliteTyrosaurEntity entity) {
            AoAScheduler.scheduleSyncronisedTask(() -> new SoundBuilder(AoASounds.ENTITY_TYROSAUR_ROAR_STOP).followEntity(entity).execute(), 3);
            ATTACK_STATE.set(entity, BITE);
            entity.setImmobile(false);
            entity.triggerAnim("Main", "roar_stop");
        }
    }
}