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
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.content.entity.animal.ShinySquidEntity;
import net.tslat.aoa3.content.entity.animal.fish.BasicFishEntity;
import net.tslat.aoa3.content.entity.animal.fish.BasicLavaFishEntity;
import net.tslat.aoa3.content.entity.animal.precasia.DeinotheriumEntity;
import net.tslat.aoa3.content.entity.animal.precasia.HorndronEntity;
import net.tslat.aoa3.content.entity.misc.PixonEntity;
import net.tslat.aoa3.content.entity.monster.nether.*;
import net.tslat.aoa3.content.entity.monster.overworld.*;
import net.tslat.aoa3.content.entity.monster.precasia.*;
import net.tslat.aoa3.content.entity.npc.trader.LottomanEntity;
import net.tslat.aoa3.content.entity.npc.trader.UndeadHeraldEntity;
import net.tslat.aoa3.util.WorldUtil;

import static net.minecraft.world.entity.SpawnPlacementTypes.*;
import static net.minecraft.world.level.levelgen.Heightmap.Types.*;

public final class AoAEntitySpawnPlacements {
    private static final SpawnPlacementType AMPHIBIOUS = (level, pos, entityType) -> (level.getFluidState(pos).isEmpty() ? ON_GROUND : IN_WATER).isSpawnPositionOk(level, pos, entityType);

    public static void init() {
        AdventOfAscension.getModEventBus().addListener(AoAEntitySpawnPlacements::registerSpawnPlacements);
    }

    @SubscribeEvent
    private static void registerSpawnPlacements(final RegisterSpawnPlacementsEvent ev) {
        setOverworldSpawnPlacements(ev);
        setNetherSpawnPlacements(ev);
        setPrecasiaSpawnPlacements(ev);
        setMiscSpawnPlacements(ev);
    }

    private static void setOverworldSpawnPlacements(final RegisterSpawnPlacementsEvent ev) {
        register(ev, AoAMonsters.ANCIENT_GOLEM.get(), AncientGolemEntity.spawnRules());
        register(ev, AoAMonsters.BOMB_CARRIER.get(), BombCarrierEntity.spawnRules());
        register(ev, AoAMonsters.BUSH_BABY.get(), ON_GROUND, MOTION_BLOCKING, BushBabyEntity.spawnRules());
        register(ev, AoAMonsters.CHARGER.get(), ChargerEntity.spawnRules());
        register(ev, AoAMonsters.CHOMPER.get(), AMPHIBIOUS, MOTION_BLOCKING_NO_LEAVES, ChomperEntity.spawnRules());
        register(ev, AoAMonsters.CYCLOPS.get(), CyclopsEntity.spawnRules());
        register(ev, AoAMonsters.GHOST.get(), GhostEntity.spawnRules());
        register(ev, AoAMonsters.GOBLIN.get(), GoblinEntity.spawnRules());
        register(ev, AoAMonsters.ICE_GIANT.get(), IceGiantEntity.spawnRules());
        register(ev, AoAMonsters.KING_CHARGER.get(), KingChargerEntity.spawnRules());
        register(ev, AoAMonsters.LEAFY_GIANT.get(), LeafyGiantEntity.spawnRules());
        register(ev, AoAMonsters.SAND_GIANT.get(), SandGiantEntity.spawnRules());
        register(ev, AoAMonsters.SASQUATCH.get(), SasquatchEntity.spawnRules());
        register(ev, AoAMonsters.STONE_GIANT.get(), StoneGiantEntity.spawnRules());
        register(ev, AoAMonsters.TREE_SPIRIT.get(), TreeSpiritEntity.spawnRules());
        register(ev, AoAMonsters.VOID_WALKER.get(), VoidWalkerEntity.spawnRules());
        register(ev, AoAMonsters.WOOD_GIANT.get(), WoodGiantEntity.spawnRules());
        register(ev, AoAMonsters.YETI.get(), YetiEntity.spawnRules());
    }

    private static void setNetherSpawnPlacements(final RegisterSpawnPlacementsEvent ev) {
        register(ev, AoAMonsters.EMBRAKE.get(), EmbrakeEntity.spawnRules());
        register(ev, AoAMonsters.FLAMEWALKER.get(), FlamewalkerEntity.spawnRules());
        register(ev, AoAMonsters.INFERNAL.get(), InfernalEntity.spawnRules());
        register(ev, AoAMonsters.LITTLE_BAM.get(), LittleBamEntity.spawnRules());
        register(ev, AoAMonsters.NETHENGEIC_BEAST.get(), NethengeicBeastEntity.spawnRules());
    }

    private static void setPrecasiaSpawnPlacements(final RegisterSpawnPlacementsEvent ev) {
        register(ev, AoAMonsters.SPINOLEDON.get(), SpinoledonEntity.spawnRules());
        register(ev, AoAAnimals.HORNDRON.get(), HorndronEntity.spawnRules());
        register(ev, AoAAnimals.DEINOTHERIUM.get(), DeinotheriumEntity.spawnRules());
        register(ev, AoAMonsters.MEGANEUROPSIS.get(), NO_RESTRICTIONS, MOTION_BLOCKING, MeganeuropsisEntity.spawnRules());
        register(ev, AoAMonsters.SCOLOPENDIS.get(), ScolopendisEntity.spawnRules());
        register(ev, AoAMonsters.SMILODON.get(), SmilodonEntity.spawnRules());
        register(ev, AoAMonsters.ATTERCOPUS.get(), AttercopusEntity.spawnRules());
        register(ev, AoAMonsters.VELORAPTOR.get(), VeloraptorEntity.spawnRules());
        register(ev, AoAMonsters.DUNKLEOSTEUS.get(), IN_WATER, OCEAN_FLOOR, DunkleosteusEntity.spawnRules());
    }

    private static void setMiscSpawnPlacements(final RegisterSpawnPlacementsEvent ev) {
        register(ev, AoAAnimals.SHINY_SQUID.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, ShinySquidEntity.spawnRules());
        register(ev, EntityType.SNIFFER, SpawnBuilder.DEFAULT_ANIMAL);
        register(ev, AoANpcs.LOTTOMAN.get(), LottomanEntity.spawnRules());
        register(ev, AoANpcs.UNDEAD_HERALD.get(), UndeadHeraldEntity.spawnRules());
        register(ev, AoAMiscEntities.PIXON.get(), PixonEntity.spawnRules());

        register(ev, AoAAnimals.BLUE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.CANDLEFISH.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, BasicLavaFishEntity.spawnRules());
        register(ev, AoAAnimals.CHARRED_CHAR.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, BasicLavaFishEntity.spawnRules());
        register(ev, AoAAnimals.CHOCAW.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.CRIMSON_SKIPPER.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, BasicLavaFishEntity.spawnRules());
        register(ev, AoAAnimals.CRIMSON_STRIPEFISH.get(), IN_LAVA, MOTION_BLOCKING_NO_LEAVES, BasicLavaFishEntity.spawnRules());
        register(ev, AoAAnimals.DARK_HATCHETFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.GREEN_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.HYDRONE.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.IRONBACK.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.JAMFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.PARAPIRANHA.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.PEARL_STRIPEFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.PURPLE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.RAINBOWFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.RAZORFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.RED_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.REEFTOOTH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.ROCKETFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.SAILBACK.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.SAPPHIRE_STRIDER.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.SKELECANTH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.WHITE_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.YELLOW_GEMTRAP.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.TURQUOISE_STRIPEFISH.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
        register(ev, AoAAnimals.VIOLET_SKIPPER.get(), IN_WATER, MOTION_BLOCKING_NO_LEAVES, BasicFishEntity.spawnRules());
    }

    private static <T extends Entity> void register(RegisterSpawnPlacementsEvent ev, EntityType<T> entityType, SpawnPlacements.SpawnPredicate<?> predicate) {
        register(ev, entityType, ON_GROUND, MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Entity> void register(RegisterSpawnPlacementsEvent ev, EntityType<T> entityType, SpawnPlacementType type, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<?> predicate) {
        ev.register(entityType, type, heightmap, (SpawnPlacements.SpawnPredicate<T>)predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    public static final class SpawnBuilder<T extends Entity> implements SpawnPlacements.SpawnPredicate<T> {
        public static final SpawnBuilder<Entity> DEFAULT = new SpawnBuilder<>();
        public static final SpawnBuilder<Mob> DEFAULT_MONSTER = DEFAULT.noPeacefulSpawn().defaultMonsterLightLevels().ifValidSpawnBlock();
        public static final SpawnBuilder<Mob> DEFAULT_DAY_NIGHT_MONSTER = DEFAULT.noPeacefulSpawn().defaultMonsterBlockLightLevels().ifValidSpawnBlock();
        public static final SpawnBuilder<Mob> DEFAULT_DAY_MONSTER = DEFAULT_DAY_NIGHT_MONSTER.onlyDuringDay().difficultyBasedSpawnChance(0.12f);
        public static final SpawnBuilder<Entity> DEFAULT_ANIMAL = DEFAULT.animalSpawnRules();

        private final SpawnPlacements.SpawnPredicate<T> predicate;

        SpawnBuilder() {
            this((entityType, world, spawnType, pos, rand) -> true);
        }

        public SpawnBuilder(SpawnPlacements.SpawnPredicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(EntityType<T> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
            return this.predicate.test(entityType, level, spawnType, pos, rand);
        }

        public SpawnBuilder<T> and(SpawnPlacements.SpawnPredicate<T> predicate) {
            return new SpawnBuilder<>((entityType, level, spawnType, pos, rand) -> this.predicate.test(entityType, level, spawnType, pos, rand) && predicate.test(entityType, level, spawnType, pos, rand));
        }

        public SpawnBuilder<T> animalSpawnRules() {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && (MobSpawnType.ignoresLightRequirements(spawnType) || level.getRawBrightness(pos, 0) > 8));
        }

        public SpawnBuilder<T> noPeacefulSpawn() {
            return and((entityType, level, spawnType, pos, rand) -> level.getDifficulty() != Difficulty.PEACEFUL);
        }

        public <M extends Mob> SpawnBuilder<M> ifValidSpawnBlock() {
            return and((SpawnPlacements.SpawnPredicate)(entityType, level, spawnType, pos, rand) -> Mob.checkMobSpawnRules((EntityType<M>)entityType, level, spawnType, pos, rand));
        }

        public SpawnBuilder<T> noLowerThanY(int minY) {
            return and((entityType, level, spawnType, pos, rand) -> pos.getY() >= minY);
        }

        public SpawnBuilder<T> noHigherThanY(int maxY) {
            return and((entityType, level, spawnType, pos, rand) -> pos.getY() <= maxY);
        }

        public SpawnBuilder<T> betweenYLevels(int minY, int maxY) {
            return and((entityType, level, spawnType, pos, rand) -> pos.getY() >= minY && pos.getY() <= maxY);
        }

        public SpawnBuilder<T> spawnChance(float chance) {
            return and((entityType, level, spawnType, pos, rand) -> rand.nextFloat() < chance);
        }

        public SpawnBuilder<T> difficultyBasedSpawnChance(float chance) {
            return and((entityType, level, spawnType, pos, rand) -> rand.nextFloat() < chance * level.getCurrentDifficultyAt(pos).getEffectiveDifficulty());
        }

        public SpawnBuilder<T> noSpawnOn(TagKey<Block> blockTag) {
            return and((entityType, level, spawnType, pos, rand) -> !level.getBlockState(pos.below()).is(blockTag));
        }

        public SpawnBuilder<T> noSpawnOn(Block block) {
            return and((entityType, level, spawnType, pos, rand) -> !level.getBlockState(pos.below()).is(block));
        }

        public SpawnBuilder<T> onlySpawnOn(TagKey<Block> blockTag) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos.below()).is(blockTag));
        }

        public SpawnBuilder<T> onlySpawnOn(Block block) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos.below()).is(block));
        }

        public SpawnBuilder<T> onlySpawnIn(TagKey<Block> blockTag) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos).is(blockTag));
        }

        public SpawnBuilder<T> onlySpawnIn(Block block) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos).is(block));
        }

        public SpawnBuilder<T> onlySpawnUnder(TagKey<Block> blockTag) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos.above()).is(blockTag));
        }

        public SpawnBuilder<T> onlySpawnUnder(Block block) {
            return and((entityType, level, spawnType, pos, rand) -> level.getBlockState(pos.above()).is(block));
        }

        public SpawnBuilder<T> minLightLevel(int lightLevel) {
            return and((entityType, level, spawnType, pos, rand) -> WorldUtil.getLightLevel(level, pos, false, false) >= lightLevel);
        }

        public SpawnBuilder<T> maxLightLevel(int lightLevel) {
            return and((entityType, level, spawnType, pos, rand) -> WorldUtil.getLightLevel(level, pos, false, false) <= lightLevel);
        }

        public SpawnBuilder<T> defaultMonsterLightLevels() {
            return and((entityType, level, spawnType, pos, rand) -> Monster.isDarkEnoughToSpawn(level, pos, rand));
        }

        public SpawnBuilder<T> defaultMonsterBlockLightLevels() {
            return and((entityType, level, spawnType, pos, rand) -> WorldUtil.getLightLevel(level, pos, true, false) <= level.dimensionType().monsterSpawnBlockLightLimit());
        }

        public SpawnBuilder<T> onlyDuringDay() {
            return and((entityType, level, spawnType, pos, rand) -> level.getLevel().isDay());
        }
    }
}
