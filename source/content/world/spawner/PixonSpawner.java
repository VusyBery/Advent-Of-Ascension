package net.tslat.aoa3.content.world.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.entity.AoACustomSpawners;
import net.tslat.aoa3.common.registration.entity.AoAMiscEntities;
import net.tslat.aoa3.common.registration.entity.variant.PixonVariant;
import net.tslat.aoa3.content.entity.misc.PixonEntity;

import java.util.Optional;

public class PixonSpawner implements AoACustomSpawner<PixonEntity> {
	public static final MapCodec<PixonSpawner> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			AoACustomSpawner.GENERIC_SETTINGS_CODEC.fieldOf("base_settings").forGetter(spawner -> spawner.baseSettings),
			Codec.lazyInitialized(() -> SimpleWeightedRandomList.wrappedCodec(AoARegistries.PIXON_VARIANTS.lookupCodec())).fieldOf("variants").forGetter(spawner -> spawner.variants)
	).apply(builder, PixonSpawner::new));

	private final GenericSettings baseSettings;
	private final SimpleWeightedRandomList<PixonVariant> variants;

	private long nextSpawnTick = -1;

	public PixonSpawner(GenericSettings baseSettings, SimpleWeightedRandomList<PixonVariant> variants) {
		this.baseSettings = baseSettings;
		this.variants = variants;
	}

	@Override
	public boolean shouldAddToDimension(ServerLevel level) {
		if (level.isFlat() && !this.baseSettings.spawnInSuperflat())
			return false;

		final ResourceKey<Level> dimension = level.dimension();

		if (!dimension.location().getNamespace().equals("minecraft") && !AdventOfAscension.isAoA(dimension.location()))
			return false;

		if ((this.baseSettings.inDimensions().isEmpty() || this.baseSettings.inDimensions().get().isEmpty()) &&
				(this.baseSettings.notInDimensions().isEmpty() || this.baseSettings.notInDimensions().get().isEmpty()))
			return true;

		return this.baseSettings.inDimensions().map(set -> set.contains(dimension)).orElse(true) &&
				this.baseSettings.notInDimensions().map(set -> !set.contains(dimension)).orElse(true);
	}

	@Override
	public PixonSpawner copy() {
		return new PixonSpawner(this.baseSettings, this.variants);
	}

	@Override
	public Type getType() {
		return AoACustomSpawners.PIXONS.get();
	}

	@Override
	public int tick(ServerLevel level, boolean spawnHostiles, boolean spawnPassives) {
		if (this.nextSpawnTick > level.getGameTime() || !spawnPassives || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING))
			return 0;

		RandomSource random = level.getRandom();
		this.nextSpawnTick = level.getGameTime() + this.baseSettings.spawnInterval().sample(random);

		return doSpawning(level, random);
	}

	private int doSpawning(ServerLevel level, RandomSource random) {
		int count = 0;

		for (ServerPlayer pl : level.getPlayers(pl -> !pl.isSpectator() && pl.isAlive())) {
			if (level.getRandom().nextFloat() >= this.baseSettings.chancePerPlayer())
				continue;

			for (Pair<EntityType<PixonEntity>, BlockPos> spawn : findNearbySpawnPositions(level, random, pl.blockPosition(), 40, 128, this.baseSettings.spawnAttemptsPerPlayer().sample(random) + (int)pl.getAttributeValue(Attributes.LUCK), () -> Optional.of(AoAMiscEntities.PIXON.get()))) {
				if (this.baseSettings.spawnRules().isPresent()) {
					SpawnData.CustomSpawnRules spawnRules = this.baseSettings.spawnRules().get();

					if (!spawnRules.blockLightLimit().isValueInRange(level.getBrightness(LightLayer.BLOCK, spawn.right())) || !spawnRules.skyLightLimit().isValueInRange(level.getBrightness(LightLayer.SKY, spawn.right())))
						continue;
				}

				PixonEntity pixon = spawn.left().create(level, null, spawn.right(), MobSpawnType.NATURAL, false, false);

				if (pixon == null)
					continue;

 				pixon.finalizeSpawn(level, level.getCurrentDifficultyAt(pixon.blockPosition()), this.variants.getRandom(random).map(WeightedEntry.Wrapper::data).orElse(null));
				level.addFreshEntityWithPassengers(pixon);

				this.nextSpawnTick += this.baseSettings.extraDelayPerSpawn().sample(random);
				count++;
			}
		}

		return count;
	}

	@Override
	public boolean canSpawnAt(EntityType entityType, ServerLevel level, RandomSource random, BlockPos pos, SpawnPlacementType spawnPlacement) {
		return canSpawnInBiome(level, pos) && canSpawnOn(level, level.getBlockState(pos.below()), pos.below()) && canSpawnInside(level, level.getBlockState(pos), pos) && level.noCollision(entityType.getSpawnAABB(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d));
	}

	@Override
	public boolean canSpawnInBiome(ServerLevel level, BlockPos pos) {
		if ((this.baseSettings.inBiomes().isEmpty() || this.baseSettings.inBiomes().get().size() == 0) &&
				(this.baseSettings.notInBiomes().isEmpty() || this.baseSettings.notInBiomes().get().size() == 0))
			return true;

		Holder<Biome> biome = level.getBiome(pos);

		return this.baseSettings.inBiomes().map(set -> set.contains(biome)).orElse(true) &&
				this.baseSettings.notInBiomes().map(set -> !set.contains(biome)).orElse(true);
	}

	private static boolean canSpawnOn(ServerLevel level, BlockState state, BlockPos pos) {
		return state.isValidSpawn(level, pos, AoAMiscEntities.PIXON.get());
	}

	private static boolean canSpawnInside(ServerLevel level, BlockState state, BlockPos pos) {
		if (state.isCollisionShapeFullBlock(level, pos))
			return false;

		if (state.isSignalSource() || !level.getFluidState(pos).isEmpty())
			return false;

		if (state.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE))
			return false;

		return true;
	}
}
