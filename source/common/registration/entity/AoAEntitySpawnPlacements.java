package net.tslat.aoa3.common.registration.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.worldgen.AoABiomes;
import net.tslat.aoa3.content.entity.monster.nether.NethengeicBeastEntity;
import net.tslat.aoa3.util.WorldUtil;

import static net.minecraft.world.entity.SpawnPlacementTypes.*;
import static net.minecraft.world.level.levelgen.Heightmap.Types.*;

public final class AoAEntitySpawnPlacements {
    private static final SpawnPlacementType AMPHIBIOUS = (level, pos, entityType) -> (level.getFluidState(pos).isEmpty() ? ON_GROUND : IN_WATER).isSpawnPositionOk(level, pos, entityType);

    public static void init() {
        AdventOfAscension.getModEventBus().addListener(AoAEntitySpawnPlacements::registerSpawnPlacements);
    }

    @SubscribeEvent
    private static void registerSpawnPlacements(final SpawnPlacementRegisterEvent ev) {
        setOverworldSpawnPlacements(ev);
        setNetherSpawnPlacements(ev);
        setPrecasiaSpawnPlacements(ev);
        setMiscSpawnPlacements(ev);
    }

    private static void setOverworldSpawnPlacements(final SpawnPlacementRegisterEvent ev) {
        register(ev, AoAMonsters.ANCIENT_GOLEM.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(65));
        register(ev, AoAMonsters.BOMB_CARRIER.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(55).spawnChance(1 / 5f));
        register(ev, AoAMonsters.BUSH_BABY.get(), ON_GROUND, MOTION_BLOCKING, SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(65));
        register(ev, AoAMonsters.CHARGER.get(), SpawnBuilder.DEFAULT_DAY_MONSTER);
        register(ev, AoAMonsters.CHOMPER.get(), AMPHIBIOUS, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.difficultyBasedSpawnChance(0.1f));
        register(ev, AoAMonsters.CYCLOPS.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(55));
        register(ev, AoAMonsters.GHOST.get(), SpawnBuilder.DEFAULT_MONSTER.noHigherThanY(0).spawnChance(1 / 2f));
        register(ev, AoAMonsters.GOBLIN.get(), SpawnBuilder.DEFAULT_DAY_MONSTER);
        register(ev, AoAMonsters.ICE_GIANT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 15f));
        register(ev, AoAMonsters.KING_CHARGER.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 16f));
        register(ev, AoAMonsters.LEAFY_GIANT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 15f));
        register(ev, AoAMonsters.SAND_GIANT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 15f));
        register(ev, AoAMonsters.SASQUATCH.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(55));
        register(ev, AoAMonsters.STONE_GIANT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 15f));
        register(ev, AoAMonsters.TREE_SPIRIT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(55).spawnChance(1 / 10f));
        register(ev, AoAMonsters.VOID_WALKER.get(), SpawnBuilder.DEFAULT_MONSTER.noHigherThanY(0));
        register(ev, AoAMonsters.WOOD_GIANT.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.spawnChance(1 / 15f));
        register(ev, AoAMonsters.YETI.get(), SpawnBuilder.DEFAULT_DAY_MONSTER.noLowerThanY(45).spawnChance(1 / 2f));
    }

    private static void setNetherSpawnPlacements(final SpawnPlacementRegisterEvent ev) {
        register(ev, AoAMonsters.EMBRAKE.get(), new SpawnBuilder<>().noPeacefulSpawn().spawnChance(1 / 2f).noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock());
        register(ev, AoAMonsters.FLAMEWALKER.get(), new SpawnBuilder<>().noPeacefulSpawn().noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock());
        register(ev, AoAMonsters.INFERNAL.get(), new SpawnBuilder<>().noPeacefulSpawn().spawnChance(1 / 10f).noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock());
        register(ev, AoAMonsters.LITTLE_BAM.get(), new SpawnBuilder<>().noPeacefulSpawn().spawnChance(1 / 2f).noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock());
        register(ev, AoAMonsters.NETHENGEIC_BEAST.get(), new SpawnBuilder<>().noPeacefulSpawn().noSpawnOn(Blocks.NETHER_WART_BLOCK).ifValidSpawnBlock().and(NethengeicBeastEntity::checkSpawnConditions));
    }

    private static void setPrecasiaSpawnPlacements(final SpawnPlacementRegisterEvent ev) {
        register(ev, AoAMonsters.SPINOLEDON.get(), SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.and((entityType, level, spawnType, pos, random) -> pos.getY() >= 60 || pos.getY() <= -13).difficultyBasedSpawnChance(0.05f));
        register(ev, AoAAnimals.HORNDRON.get(), SpawnBuilder.DEFAULT_ANIMAL);
        register(ev, AoAAnimals.DEINOTHERIUM.get(), SpawnBuilder.DEFAULT_ANIMAL);
        register(ev, AoAMonsters.MEGANEUROPSIS.get(), NO_RESTRICTIONS, MOTION_BLOCKING, SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.noLowerThanY(65).difficultyBasedSpawnChance(0.05f));
        register(ev, AoAMonsters.SMILODON.get(), SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.noLowerThanY(60).difficultyBasedSpawnChance(0.1f));
        register(ev, AoAMonsters.ATTERCOPUS.get(), SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.and((entityType, level, spawnType, pos, rand) -> (level.getBiome(pos).is(AoABiomes.PRECASIAN_DESERT) && level.getSkyDarken() >= 4 && rand.nextFloat() < 0.05f * level.getCurrentDifficultyAt(pos).getEffectiveDifficulty()) || pos.getY() <= 50));
        register(ev, AoAMonsters.VELORAPTOR.get(), SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.noLowerThanY(60).difficultyBasedSpawnChance(0.1f));
        register(ev, AoAMonsters.DUNKLEOSTEUS.get(), IN_WATER, OCEAN_FLOOR, SpawnBuilder.DEFAULT_DAY_NIGHT_MONSTER.noHigherThanY(55).difficultyBasedSpawnChance(0.05f));
    }

    private static void setMiscSpawnPlacements(final SpawnPlacementRegisterEvent ev) {
        register(ev, AoAAnimals.SHINY_SQUID.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, new SpawnBuilder<>(GlowSquid::checkGlowSquidSpawnRules).spawnChance(1 / 1000f));
        register(ev, EntityType.SNIFFER, SpawnBuilder.DEFAULT_ANIMAL);
        register(ev, AoANpcs.LOTTOMAN.get(), new SpawnBuilder<>().ifValidSpawnBlock());
        register(ev, AoANpcs.UNDEAD_HERALD.get(), new SpawnBuilder<>().ifValidSpawnBlock());
        register(ev, AoAMiscEntities.PIXON.get(), new SpawnBuilder<>());

        register(ev, AoAAnimals.BLUE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.CANDLEFISH.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_LAVA_FISH);
        register(ev, AoAAnimals.CHARRED_CHAR.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_LAVA_FISH);
        register(ev, AoAAnimals.CHOCAW.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.CRIMSON_SKIPPER.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_LAVA_FISH);
        register(ev, AoAAnimals.CRIMSON_STRIPEFISH.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_LAVA_FISH);
        register(ev, AoAAnimals.DARK_HATCHETFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.GREEN_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.HYDRONE.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.IRONBACK.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.JAMFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.PARAPIRANHA.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.PEARL_STRIPEFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.PURPLE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.RAINBOWFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.RAZORFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.RED_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.REEFTOOTH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.ROCKETFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.SAILBACK.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.SAPPHIRE_STRIDER.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.SKELECANTH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.WHITE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.YELLOW_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.TURQUOISE_STRIPEFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
        register(ev, AoAAnimals.VIOLET_SKIPPER.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, SpawnBuilder.DEFAULT_FISH);
    }

    private static <T extends Entity> void register(SpawnPlacementRegisterEvent ev, EntityType<T> entityType, SpawnPlacements.SpawnPredicate<?> predicate) {
        register(ev, entityType, ON_GROUND, MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Entity> void register(SpawnPlacementRegisterEvent ev, EntityType<T> entityType, SpawnPlacementType type, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<?> predicate) {
        ev.register(entityType, type, heightmap, (SpawnPlacements.SpawnPredicate<T>)predicate, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    private static final class SpawnBuilder<T extends Entity> implements SpawnPlacements.SpawnPredicate<T> {
        static final SpawnBuilder<Entity> DEFAULT_FISH = new SpawnBuilder<>().onlySpawnIn(Blocks.WATER).onlySpawnUnder(Blocks.WATER);
        static final SpawnBuilder<Entity> DEFAULT_LAVA_FISH = new SpawnBuilder<>().onlySpawnIn(Blocks.LAVA).onlySpawnUnder(Blocks.LAVA);
        static final SpawnBuilder<Mob> DEFAULT_MONSTER = new SpawnBuilder<>().noPeacefulSpawn().defaultMonsterLightLevels().ifValidSpawnBlock();
        static final SpawnBuilder<Mob> DEFAULT_DAY_NIGHT_MONSTER = new SpawnBuilder<>().noPeacefulSpawn().defaultMonsterBlockLightLevels().ifValidSpawnBlock();
        static final SpawnBuilder<Mob> DEFAULT_DAY_MONSTER = DEFAULT_DAY_NIGHT_MONSTER.onlyDuringDay().difficultyBasedSpawnChance(0.12f);
        static final SpawnBuilder<Entity> DEFAULT_ANIMAL = new SpawnBuilder<>().animalSpawnRules();

        private final SpawnPlacements.SpawnPredicate<T> predicate;

        SpawnBuilder() {
            this((entityType, world, spawnType, pos, rand) -> true);
        }

        SpawnBuilder(SpawnPlacements.SpawnPredicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(EntityType<T> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
            return this.predicate.test(entityType, level, spawnType, pos, rand);
        }

        SpawnBuilder<T> animalSpawnRules() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8);
        }

        SpawnBuilder<T> noPeacefulSpawn() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getDifficulty() != Difficulty.PEACEFUL);
        }

        <M extends Mob> SpawnBuilder<M> ifValidSpawnBlock() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test((EntityType)entityType, level, spawnType, pos, rand) && Mob.checkMobSpawnRules(entityType, level, spawnType, pos, rand));
        }

        SpawnBuilder<T> noLowerThanY(int minY) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && pos.getY() >= minY);
        }

        SpawnBuilder<T> noHigherThanY(int maxY) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && pos.getY() <= maxY);
        }

        SpawnBuilder<T> betweenYLevels(int minY, int maxY) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && pos.getY() >= minY && pos.getY() <= maxY);
        }

        SpawnBuilder<T> spawnChance(float chance) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) ->  this.predicate.test(entityType, level, spawnType, pos, rand) && rand.nextFloat() < chance);
        }

        SpawnBuilder<T> difficultyBasedSpawnChance(float chance) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && rand.nextFloat() < chance * level.getCurrentDifficultyAt(pos).getEffectiveDifficulty());
        }

        SpawnBuilder<T> noSpawnOn(TagKey<Block> blockTag) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && !level.getBlockState(pos.below()).is(blockTag));
        }

        SpawnBuilder<T> noSpawnOn(Block block) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && !level.getBlockState(pos.below()).is(block));
        }

        SpawnBuilder<T> onlySpawnOn(TagKey<Block> blockTag) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos.below()).is(blockTag));
        }

        SpawnBuilder<T> onlySpawnOn(Block block) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos.below()).is(block));
        }

        SpawnBuilder<T> onlySpawnIn(TagKey<Block> blockTag) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos).is(blockTag));
        }

        SpawnBuilder<T> onlySpawnIn(Block block) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos).is(block));
        }

        SpawnBuilder<T> onlySpawnUnder(TagKey<Block> blockTag) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos.above()).is(blockTag));
        }

        SpawnBuilder<T> onlySpawnUnder(Block block) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getBlockState(pos.above()).is(block));
        }

        SpawnBuilder<T> minLightLevel(int lightLevel) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && WorldUtil.getLightLevel(level, pos, false, false) >= lightLevel);
        }

        SpawnBuilder<T> maxLightLevel(int lightLevel) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && WorldUtil.getLightLevel(level, pos, false, false) <= lightLevel);
        }

        SpawnBuilder<T> defaultMonsterLightLevels() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && Monster.isDarkEnoughToSpawn(level, pos, rand));
        }

        SpawnBuilder<T> defaultMonsterBlockLightLevels() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && WorldUtil.getLightLevel(level, pos, true, false) <= level.dimensionType().monsterSpawnBlockLightLimit());
        }

        SpawnBuilder<T> onlyDuringDay() {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && level.getLevel().isDay());
        }

        SpawnBuilder<T> and(SpawnPlacements.SpawnPredicate<T> predicate) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && predicate.test(entityType, level, spawnType, pos, rand));
        }
    }
}
