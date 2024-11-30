package net.tslat.aoa3.content.skill.hauling;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.library.object.RandomEntryPool;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HaulingSpawnPool extends RandomEntryPool<HaulingEntity, ServerPlayer> {
    public static final Codec<HaulingSpawnPool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(HaulingSpawnPool::getBiomes),
            NeoForgeRegistries.FLUID_TYPES.byNameCodec().fieldOf("for_fluid").forGetter(HaulingSpawnPool::getFluidType),
            Codec.BOOL.optionalFieldOf("is_default", false).forGetter(HaulingSpawnPool::isDefaultForFluid),
            Codec.BOOL.optionalFieldOf("is_trap", false).forGetter(HaulingSpawnPool::isTrap),
            HaulingEntity.CODEC.xmap(HaulingEntity::toPoolEntry, PoolEntry::get).listOf().fieldOf("entries").forGetter(pool -> pool.entries)
    ).apply(instance, HaulingSpawnPool::new));
    private static final Map<FluidType, Map<Holder<Biome>, HaulingSpawnPool>> POOL_LOOKUP = new Object2ObjectOpenHashMap<>();
    private static final Map<FluidType, Map<Holder<Biome>, HaulingSpawnPool>> TRAP_POOL_LOOKUP = new Object2ObjectOpenHashMap<>();

    static {
        NeoForge.EVENT_BUS.addListener(ServerStoppedEvent.class, ev -> {
            POOL_LOOKUP.clear();
            TRAP_POOL_LOOKUP.clear();
        });
    }

    private final HolderSet<Biome> biomes;
    private final FluidType fluidType;
    private final boolean isDefaultForFluid;
    private final boolean isTrap;

    HaulingSpawnPool(HolderSet<Biome> forBiomes, FluidType fluidType, boolean isDefaultForFluid, boolean isTrap, List<PoolEntry<HaulingEntity, ServerPlayer>> entries) {
        super(entries, RandomSource.create());

        this.biomes = forBiomes;
        this.fluidType = fluidType;
        this.isDefaultForFluid = isDefaultForFluid;
        this.isTrap = isTrap;
    }

    public HolderSet<Biome> getBiomes() {
        return this.biomes;
    }

    public FluidType getFluidType() {
        return this.fluidType;
    }

    public boolean isDefaultForFluid() {
        return this.isDefaultForFluid;
    }

    public boolean isTrap() {
        return this.isTrap;
    }

    public static Optional<HaulingSpawnPool> getPoolForLocation(Level level, BlockPos pos, FluidType forFluid) {
        return getPoolForBiome(level, level.getBiome(pos), forFluid);
    }

    public static Optional<HaulingSpawnPool> getPoolForBiome(Level level, Holder<Biome> biome, FluidType forFluid) {
        if (POOL_LOOKUP.isEmpty()) {
            level.registryAccess().registry(AoARegistries.HAULING_SPAWN_POOLS_REGISTRY_KEY).orElseThrow().stream()
                    .filter(pool -> !pool.isTrap())
                    .forEach(pool -> {
                        final Map<Holder<Biome>, HaulingSpawnPool> pools = POOL_LOOKUP.computeIfAbsent(pool.getFluidType(), key -> new Object2ObjectOpenHashMap<>());

                        if (pool.isDefaultForFluid()) {
                            pools.put(null, pool);
                        }
                        else {
                            pool.getBiomes().forEach(poolBiome -> pools.put(poolBiome, pool));
                        }
                    });
        }

        final Map<Holder<Biome>, HaulingSpawnPool> pool = POOL_LOOKUP.get(forFluid);

        if (pool == null)
            return Optional.empty();

        if (pool.containsKey(biome))
            return Optional.of(pool.get(biome));

        return Optional.ofNullable(pool.get(null));
    }

    public static Optional<HaulingSpawnPool> getTrapsPoolForLocation(Level level, BlockPos pos, FluidType forFluid) {
        return getPoolForBiome(level, level.getBiome(pos), forFluid);
    }

    public static Optional<HaulingSpawnPool> getTrapsPoolForBiome(Level level, Holder<Biome> biome, FluidType forFluid) {
        if (TRAP_POOL_LOOKUP.isEmpty()) {
            level.registryAccess().registry(AoARegistries.HAULING_SPAWN_POOLS_REGISTRY_KEY).orElseThrow().stream()
                    .filter(HaulingSpawnPool::isTrap)
                    .forEach(pool -> {
                        final Map<Holder<Biome>, HaulingSpawnPool> pools = TRAP_POOL_LOOKUP.computeIfAbsent(pool.getFluidType(), key -> new Object2ObjectOpenHashMap<>());

                        if (pool.isDefaultForFluid()) {
                            pools.put(null, pool);
                        }
                        else {
                            pool.getBiomes().forEach(poolBiome -> pools.put(poolBiome, pool));
                        }
                    });
        }

        final Map<Holder<Biome>, HaulingSpawnPool> pool = TRAP_POOL_LOOKUP.get(forFluid);

        if (pool == null)
            return Optional.empty();

        if (pool.containsKey(biome))
            return Optional.of(pool.get(biome));

        return Optional.ofNullable(pool.get(null));
    }

    public static class Builder {
        private final FluidType fluidType;
        private final List<PoolEntry<HaulingEntity, ServerPlayer>> entries = new ObjectArrayList<>();
        private final HolderSet<Biome> forBiomes;
        private boolean isTrap = false;

        Builder(Holder<FluidType> fluidType, HolderSet<Biome> forBiomes) {
            this.fluidType = fluidType.value();
            this.forBiomes = forBiomes;
        }

        public static Builder forFluid(Holder<FluidType> fluidType, HolderSet<Biome> forBiomes) {
            return new Builder(fluidType, forBiomes);
        }

        public static Builder forFluidAsDefaultPool(Holder<FluidType> fluidType) {
            return forFluid(fluidType, HolderSet.empty());
        }

        public Builder isTrapPool() {
            this.isTrap = true;

            return this;
        }

        public Builder entity(EntityType<?> entity) {
            return entity(entity, 1, 1);
        }

        public Builder entity(EntityType<?> entity, int weight) {
            return entity(entity, null, weight, 0, 1);
        }

        public Builder entity(EntityType<?> entity, int weight, float weightMod) {
            return entity(entity, null, weight, weightMod, 1);
        }

        public Builder entity(EntityType<?> entity, int weight, int level) {
            return entity(entity, null, weight, 0, level);
        }

        public Builder entity(EntityType<?> entity, int weight, float weightMod, int level) {
            return entity(entity, null, weight, weightMod, level);
        }

        public Builder entity(EntityType<?> entity, CompoundTag spawnData) {
            return entity(entity, spawnData, 1, 1);
        }

        public Builder entity(EntityType<?> entity, CompoundTag spawnData, int weight) {
            return entity(entity, spawnData, weight, 0, 1);
        }

        public Builder entity(EntityType<?> entity, CompoundTag spawnData, int weight, float weightMod) {
            return entity(entity, spawnData, weight, weightMod, 1);
        }

        public Builder entity(EntityType<?> entity, CompoundTag spawnData, int weight, int level) {
            return entity(entity, spawnData, weight, 0, level);
        }

        public Builder entity(EntityType<?> entity, @Nullable CompoundTag spawnData, int weight, float weightMod, int level) {
            this.entries.add(new HaulingEntity(Either.right(entity), Optional.ofNullable(spawnData), weight, level, weightMod).toPoolEntry());

            return this;
        }

        public Builder item(ItemLike item) {
            return item(item, 1, 1);
        }

        public Builder item(ItemLike entity, int weight) {
            return item(entity, weight, 1);
        }

        public Builder item(ItemLike entity, int weight, float weightMod) {
            return item(entity, weight, weightMod, 1);
        }

        public Builder item(ItemLike item, int weight, int level) {
            return item(item, weight, 0, level);
        }

        public Builder item(ItemLike item, int weight, float weightMod, int level) {
            return item(item.asItem().getDefaultInstance(), weight, weightMod, level);
        }

        public Builder item(ItemStack item) {
            return item(item, 1, 1);
        }

        public Builder item(ItemStack entity, int weight) {
            return item(entity, weight, 1);
        }

        public Builder item(ItemStack entity, int weight, float weightMod) {
            return item(entity, weight, weightMod, 1);
        }

        public Builder item(ItemStack item, int weight, int level) {
            return item(item, weight, 0, level);
        }

        public Builder item(ItemStack item, int weight, float weightMod, int level) {
            this.entries.add(new HaulingEntity(Either.left(item), Optional.empty(), weight, level, weightMod).toPoolEntry());

            return this;
        }

        public HaulingSpawnPool build() {
            return new HaulingSpawnPool(this.forBiomes, this.fluidType, this.forBiomes == HolderSet.<Biome>empty(), this.isTrap, this.entries);
        }
    }
}
