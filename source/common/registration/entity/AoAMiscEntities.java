package net.tslat.aoa3.common.registration.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.entity.misc.*;
import net.tslat.aoa3.content.entity.monster.misc.ThornyPlantSproutEntity;

public final class AoAMiscEntities {
	public static void init() {}

	//public static final DeferredHolder<EntityType<?>, EntityType<GyrocopterEntity>> GYROCOPTER = registerMiscEntity("gyrocopter", GyrocopterEntity::new, 1.375f, 1.625f, EntityType.Builder::noSummon);
	public static final DeferredHolder<EntityType<?>, EntityType<LottoTotemEntity>> LOTTO_TOTEM = register("lotto_totem", EntityTypeRegistrar.<LottoTotemEntity>misc(LottoTotemEntity::new).sized(0.75f, 0.95f).clientTrackingRange(40).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<SandGiantPitTrapEntity>> SAND_GIANT_PIT_TRAP = register("sand_giant_pit_trap", EntityTypeRegistrar.<SandGiantPitTrapEntity>misc(SandGiantPitTrapEntity::new).sized(0.875f, 0.375f).noSummon().clientTrackingRange(40).updateInterval(1).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<SandGiantSpikeTrapEntity>> SAND_GIANT_SPIKE_TRAP = register("sand_giant_spike_trap", EntityTypeRegistrar.<SandGiantSpikeTrapEntity>misc(SandGiantSpikeTrapEntity::new).sized(1.1f, 0.875f).noSummon().clientTrackingRange(40).updateInterval(1).fireImmune());

	public static final DeferredHolder<EntityType<?>, EntityType<ThornyPlantSproutEntity>> THORNY_PLANT_SPROUT = register("thorny_plant_sprout", EntityTypeRegistrar.<ThornyPlantSproutEntity>misc(ThornyPlantSproutEntity::new).sized(0.5f, 1.5f, 1.4f).clientTrackingRange(8).noSummon());

	public static final DeferredHolder<EntityType<?>, EntityType<HaulingFishingBobberEntity>> REINFORCED_BOBBER = register("reinforced_bobber", EntityTypeRegistrar.<HaulingFishingBobberEntity>misc(HaulingFishingBobberEntity::new).sized(0.25f, 0.25f).noSave().noSummon().clientTrackingRange(4).updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<ThermalFishingBobberEntity>> THERMAL_BOBBER = register("thermal_bobber", EntityTypeRegistrar.<ThermalFishingBobberEntity>misc(ThermalFishingBobberEntity::new).sized(0.25f, 0.25f).noSave().noSummon().clientTrackingRange(4).fireImmune().updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<GoldFishingBobberEntity>> GOLD_BOBBER = register("gold_bobber", EntityTypeRegistrar.<GoldFishingBobberEntity>misc(GoldFishingBobberEntity::new).sized(0.25f, 0.25f).noSave().noSummon().clientTrackingRange(4).updateInterval(5));
	public static final DeferredHolder<EntityType<?>, EntityType<FishingCageEntity>> FISHING_CAGE = register("fishing_cage", EntityTypeRegistrar.<FishingCageEntity>misc(FishingCageEntity::new).sized(0.65f, 0.63f).clientTrackingRange(40).noSummon().updateInterval(5));

	public static final DeferredHolder<EntityType<?>, EntityType<PixonEntity>> PIXON = register("pixon", EntityTypeRegistrar.<PixonEntity>misc(PixonEntity::new).sized(0.25f, 0.25f).updateInterval(Integer.MAX_VALUE).clientTrackingRange(64).fireImmune().canSpawnFarFromPlayer());

	public static final DeferredHolder<EntityType<?>, EntityType<CustomisableLightningBolt>> CUSTOMISABLE_LIGHTNING_BOLT = register("customisable_lightning_bolt", EntityTypeRegistrar.<CustomisableLightningBolt>misc(CustomisableLightningBolt::new).sized(0, 0).noSave().clientTrackingRange(16).updateInterval(Integer.MAX_VALUE));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryName, EntityTypeRegistrar<T> builder) {
        return AoARegistries.ENTITIES.register(registryName, () -> builder.build(registryName));
	}
}
