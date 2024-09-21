package net.tslat.aoa3.content.world.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.event.EventHooks;
import net.tslat.aoa3.common.registration.entity.AoACustomSpawners;

public class RoamingTraderSpawner implements AoACustomSpawner<Mob> {
	public static final MapCodec<RoamingTraderSpawner> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			AoACustomSpawner.GENERIC_SETTINGS_CODEC.fieldOf("base_settings").forGetter(spawner -> spawner.baseSettings),
			Codec.lazyInitialized(() -> WeightedRandomList.codec(MobSpawnSettings.SpawnerData.CODEC)).fieldOf("spawns").forGetter(spawner -> spawner.spawns)
	).apply(builder, RoamingTraderSpawner::new));

	private final GenericSettings baseSettings;
	private final WeightedRandomList<MobSpawnSettings.SpawnerData> spawns;

	private long nextSpawnTick = -1;

	public RoamingTraderSpawner(GenericSettings baseSettings, WeightedRandomList<MobSpawnSettings.SpawnerData> spawns) {
		this.baseSettings = baseSettings;
		this.spawns = spawns;
	}

	@Override
	public boolean shouldAddToDimension(ServerLevel level) {
		if (level.isFlat() && !this.baseSettings.spawnInSuperflat())
			return false;

		return this.baseSettings.whitelistMode() == this.baseSettings.dimensions().contains(level.dimension());
	}

	@Override
	public RoamingTraderSpawner copy() {
		return new RoamingTraderSpawner(this.baseSettings, this.spawns);
	}

	@Override
	public Type getType() {
		return AoACustomSpawners.ROAMING_TRADER.get();
	}

	@Override
	public int tick(ServerLevel level, boolean spawnHostiles, boolean spawnPassives) {
		if (this.nextSpawnTick > level.getGameTime() || !spawnPassives || !level.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING))
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

			for (Pair<EntityType<Mob>, BlockPos> spawn : findNearbySpawnPositions(level, random, pl.blockPosition(), 20, 64, this.baseSettings.spawnAttemptsPerPlayer().sample(random), () -> this.spawns.getRandom(random).map(data -> (EntityType<Mob>)data.type))) {
				EntityType<Mob> entityType = spawn.left();
				BlockPos pos = spawn.right();

				if (this.baseSettings.spawnRules().isPresent()) {
					if (!entityType.getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL)
						continue;

					SpawnData.CustomSpawnRules spawnRules = this.baseSettings.spawnRules().get();

					if (!spawnRules.blockLightLimit().isValueInRange(level.getBrightness(LightLayer.BLOCK, pos)) || !spawnRules.skyLightLimit().isValueInRange(level.getBrightness(LightLayer.SKY, pos)))
						continue;
				}

				if (!SpawnPlacements.isSpawnPositionOk(entityType, level, pos) || !SpawnPlacements.checkSpawnRules(entityType, level, MobSpawnType.NATURAL, pos, level.random) || !level.noCollision(entityType.getSpawnAABB(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d)))
					continue;

				Entity entity = entityType.create(level, null, pos, MobSpawnType.NATURAL, false, false);

				if (entity == null)
					continue;

				if (entity instanceof Mob mob) {
					if (this.baseSettings.spawnRules().isEmpty() && !mob.checkSpawnRules(level, MobSpawnType.NATURAL) || !mob.checkSpawnObstruction(level))
						continue;

					EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
				}

				level.addFreshEntityWithPassengers(entity);

				this.nextSpawnTick += this.baseSettings.extraDelayPerSpawn().sample(random);
				count++;
			}
		}

		return count;
	}
}
