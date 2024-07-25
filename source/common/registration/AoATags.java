package net.tslat.aoa3.common.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import net.tslat.aoa3.advent.AdventOfAscension;

public final class AoATags {
	public static class Blocks {
		public static final TagKey<Block> GRASS = tag("grass");
		public static final TagKey<Block> MUSHROOMS = tag("mushrooms");

		public static final TagKey<Block> INCORRECT_FOR_EMBERSTONE_TOOL = aoaTag("incorrect_for_emberstone_tool");
		public static final TagKey<Block> INCORRECT_FOR_JADE_TOOL = aoaTag("incorrect_for_jade_tool");
		public static final TagKey<Block> INCORRECT_FOR_LIMONITE_TOOL = aoaTag("incorrect_for_limonite_tool");
		public static final TagKey<Block> INCORRECT_FOR_ORNAMYTE_TOOL = aoaTag("incorrect_for_ornamyte_tool");
		public static final TagKey<Block> INCORRECT_FOR_SKELETAL_TOOL = aoaTag("incorrect_for_skeletal_tool");
		public static final TagKey<Block> INCORRECT_FOR_ENERGISTIC_TOOL = aoaTag("incorrect_for_energistic_tool");
		public static final TagKey<Block> INCORRECT_FOR_GEMCRACKER_TOOL = aoaTag("incorrect_for_gemcracker_tool");
		public static final TagKey<Block> INCORRECT_FOR_GOOFY_TOOL = aoaTag("incorrect_for_goofy_tool");
		public static final TagKey<Block> INCORRECT_FOR_OCCULT_TOOL = aoaTag("incorrect_for_occult_tool");
		public static final TagKey<Block> INCORRECT_FOR_PICKMAX_TOOL = aoaTag("incorrect_for_pickmax_tool");
		public static final TagKey<Block> INCORRECT_FOR_SOULSTONE_TOOL = aoaTag("incorrect_for_soulstone_tool");
		public static final TagKey<Block> INCORRECT_FOR_CHAINSAW_TOOL = aoaTag("incorrect_for_chainsaw_tool");
		public static final TagKey<Block> INCORRECT_FOR_DRYADS_BLESSING_TOOL = aoaTag("incorrect_for_dryads_blessing_tool");
		public static final TagKey<Block> INCORRECT_FOR_ROCK_PICK_TOOL = aoaTag("incorrect_for_rock_pick_tool");
		public static final TagKey<Block> INCORRECT_FOR_TROLL_BASHER_TOOL = aoaTag("incorrect_for_troll_basher_tool");

		public static final TagKey<Block> BARONYTE_ORE = tag("ores/baronyte");
		public static final TagKey<Block> BLAZIUM_ORE = tag("ores/blazium");
		public static final TagKey<Block> BLOODSTONE_ORE = tag("ores/bloodstone");
		public static final TagKey<Block> BLUE_GEMSTONE_ORE = tag("ores/blue_gemstone");
		public static final TagKey<Block> CHARGED_RUNIUM_ORE = tag("ores/charged_runium");
		public static final TagKey<Block> BONE_FRAGMENTS_ORE = tag("ores/chestbone_fragments");
		public static final TagKey<Block> CRYSTALLITE_ORE = tag("ores/crystallite");
		public static final TagKey<Block> ELECANIUM_ORE = tag("ores/elecanium");
		public static final TagKey<Block> EMBERSTONE_ORE = tag("ores/emberstone");
		public static final TagKey<Block> GEMENYTE_ORE = tag("ores/gemenyte");
		public static final TagKey<Block> GHASTLY_ORE = tag("ores/ghastly");
		public static final TagKey<Block> GHOULISH_ORE = tag("ores/ghoulish");
		public static final TagKey<Block> GREEN_GEMSTONE_ORE = tag("ores/green_gemstone");
		public static final TagKey<Block> JADE_ORE = tag("ores/jade");
		public static final TagKey<Block> JEWELYTE_ORE = tag("ores/jewelyte");
		public static final TagKey<Block> LIMONITE_ORE = tag("ores/limonite");
		public static final TagKey<Block> LYON_ORE = tag("ores/lyon");
		public static final TagKey<Block> MYSTITE_ORE = tag("ores/mystite");
		public static final TagKey<Block> ORNAMYTE_ORE = tag("ores/ornamyte");
		public static final TagKey<Block> PURPLE_GEMSTONE_ORE = tag("ores/purple_gemstone");
		public static final TagKey<Block> RED_GEMSTONE_ORE = tag("ores/red_gemstone");
		public static final TagKey<Block> RUNIUM_ORE = tag("ores/runium");
		public static final TagKey<Block> SHYREGEM_ORE = tag("ores/shyregem");
		public static final TagKey<Block> SHYRESTONE_ORE = tag("ores/shyrestone");
		public static final TagKey<Block> VARSIUM_ORE = tag("ores/varsium");
		public static final TagKey<Block> WHITE_GEMSTONE_ORE = tag("ores/white_gemstone");
		public static final TagKey<Block> YELLOW_GEMSTONE_ORE = tag("ores/yellow_gemstone");
		public static final TagKey<Block> STORAGE_BLOCKS_BARONYTE = tag("storage_blocks/baronyte");
		public static final TagKey<Block> STORAGE_BLOCKS_BLAZIUM = tag("storage_blocks/blazium");
		public static final TagKey<Block> STORAGE_BLOCKS_BLOODSTONE = tag("storage_blocks/bloodstone");
		public static final TagKey<Block> STORAGE_BLOCKS_CRYSTALLITE = tag("storage_blocks/crystallite");
		public static final TagKey<Block> STORAGE_BLOCKS_ELECANIUM = tag("storage_blocks/elecanium");
		public static final TagKey<Block> STORAGE_BLOCKS_EMBERSTONE = tag("storage_blocks/emberstone");
		public static final TagKey<Block> STORAGE_BLOCKS_GEMENYTE = tag("storage_blocks/gemenyte");
		public static final TagKey<Block> STORAGE_BLOCKS_GHASTLY = tag("storage_blocks/ghastly");
		public static final TagKey<Block> STORAGE_BLOCKS_GHOULISH = tag("storage_blocks/ghoulish");
		public static final TagKey<Block> STORAGE_BLOCKS_JADE = tag("storage_blocks/jade");
		public static final TagKey<Block> STORAGE_BLOCKS_JEWELYTE = tag("storage_blocks/jewelyte");
		public static final TagKey<Block> STORAGE_BLOCKS_RAW_LIMONITE = tag("storage_blocks/raw_limonite");
		public static final TagKey<Block> STORAGE_BLOCKS_RAW_EMBERSTONE = tag("storage_blocks/raw_emberstone");
		public static final TagKey<Block> STORAGE_BLOCKS_LIMONITE = tag("storage_blocks/limonite");
		public static final TagKey<Block> STORAGE_BLOCKS_LUNAR = tag("storage_blocks/lunar");
		public static final TagKey<Block> STORAGE_BLOCKS_LYON = tag("storage_blocks/lyon");
		public static final TagKey<Block> STORAGE_BLOCKS_MYSTITE = tag("storage_blocks/mystite");
		public static final TagKey<Block> STORAGE_BLOCKS_ORNAMYTE = tag("storage_blocks/ornamyte");
		public static final TagKey<Block> STORAGE_BLOCKS_SHYREGEM = tag("storage_blocks/shyregem");
		public static final TagKey<Block> STORAGE_BLOCKS_SHYRESTONE = tag("storage_blocks/shyrestone");
		public static final TagKey<Block> STORAGE_BLOCKS_SKELETAL = tag("storage_blocks/skeletal");
		public static final TagKey<Block> STORAGE_BLOCKS_VARSIUM = tag("storage_blocks/varsium");

		public static final TagKey<Block> CARVED_RUNE = aoaTag("carved_rune");
		public static final TagKey<Block> LUNAR_ORB = aoaTag("lunar_orb");
		public static final TagKey<Block> BASE_STONE_CREEPONIA = aoaTag("base_stone_creeponia");
		public static final TagKey<Block> NOWHERE_SAFE_GUI_BLOCK = aoaTag("nowhere_safe_gui_block");

		public static final TagKey<Block> NEEDS_NETHERITE_TOOL = tag("needs_netherite_tool");

		public static final TagKey<Block> BASE_STONE_PRECASIA = tag("base_stone_precasia");

		public static final TagKey<Block> EXTRACTION_TRAINABLE = tag("extraction_trainable");

		public static final TagKey<Block> INFINIBURN_BARATHOS = aoaTag("infiniburn_barathos");

		private static TagKey<Block> aoaTag(String id) {
			return BlockTags.create(AdventOfAscension.id(id));
		}

		private static TagKey<Block> tag(String id) {
			return BlockTags.create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}
	}

	public static class Items {
		public static final TagKey<Item> GRASS = tag("grass");

		public static final TagKey<Item> BARONYTE_ORE = tag("ores/baronyte");
		public static final TagKey<Item> BLAZIUM_ORE = tag("ores/blazium");
		public static final TagKey<Item> BLOODSTONE_ORE = tag("ores/bloodstone");
		public static final TagKey<Item> BLUE_GEMSTONE_ORE = tag("ores/blue_gemstone");
		public static final TagKey<Item> CHARGED_RUNIUM_ORE = tag("ores/charged_runium");
		public static final TagKey<Item> BONE_FRAGMENTS_ORE = tag("ores/chestbone_fragments");
		public static final TagKey<Item> CRYSTALLITE_ORE = tag("ores/crystallite");
		public static final TagKey<Item> ELECANIUM_ORE = tag("ores/elecanium");
		public static final TagKey<Item> EMBERSTONE_ORE = tag("ores/emberstone");
		public static final TagKey<Item> GEMENYTE_ORE = tag("ores/gemenyte");
		public static final TagKey<Item> GHASTLY_ORE = tag("ores/ghastly");
		public static final TagKey<Item> GHOULISH_ORE = tag("ores/ghoulish");
		public static final TagKey<Item> GREEN_GEMSTONE_ORE = tag("ores/green_gemstone");
		public static final TagKey<Item> JADE_ORE = tag("ores/jade");
		public static final TagKey<Item> JEWELYTE_ORE = tag("ores/jewelyte");
		public static final TagKey<Item> LIMONITE_ORE = tag("ores/limonite");
		public static final TagKey<Item> LYON_ORE = tag("ores/lyon");
		public static final TagKey<Item> MYSTITE_ORE = tag("ores/mystite");
		public static final TagKey<Item> ORNAMYTE_ORE = tag("ores/ornamyte");
		public static final TagKey<Item> PURPLE_GEMSTONE_ORE = tag("ores/purple_gemstone");
		public static final TagKey<Item> RED_GEMSTONE_ORE = tag("ores/red_gemstone");
		public static final TagKey<Item> RUNIUM_ORE = tag("ores/runium");
		public static final TagKey<Item> SHYREGEM_ORE = tag("ores/shyregem");
		public static final TagKey<Item> SHYRESTONE_ORE = tag("ores/shyrestone");
		public static final TagKey<Item> VARSIUM_ORE = tag("ores/varsium");
		public static final TagKey<Item> WHITE_GEMSTONE_ORE = tag("ores/white_gemstone");
		public static final TagKey<Item> YELLOW_GEMSTONE_ORE = tag("ores/yellow_gemstone");

		public static final TagKey<Item> STORAGE_BLOCKS_BARONYTE = tag("storage_blocks/baronyte");
		public static final TagKey<Item> STORAGE_BLOCKS_BLAZIUM = tag("storage_blocks/blazium");
		public static final TagKey<Item> STORAGE_BLOCKS_BLOODSTONE = tag("storage_blocks/bloodstone");
		public static final TagKey<Item> STORAGE_BLOCKS_CRYSTALLITE = tag("storage_blocks/crystallite");
		public static final TagKey<Item> STORAGE_BLOCKS_ELECANIUM = tag("storage_blocks/elecanium");
		public static final TagKey<Item> STORAGE_BLOCKS_EMBERSTONE = tag("storage_blocks/emberstone");
		public static final TagKey<Item> STORAGE_BLOCKS_GEMENYTE = tag("storage_blocks/gemenyte");
		public static final TagKey<Item> STORAGE_BLOCKS_GHASTLY = tag("storage_blocks/ghastly");
		public static final TagKey<Item> STORAGE_BLOCKS_GHOULISH = tag("storage_blocks/ghoulish");
		public static final TagKey<Item> STORAGE_BLOCKS_JADE = tag("storage_blocks/jade");
		public static final TagKey<Item> STORAGE_BLOCKS_JEWELYTE = tag("storage_blocks/jewelyte");
		public static final TagKey<Item> STORAGE_BLOCKS_RAW_LIMONITE = tag("storage_blocks/raw_limonite");
		public static final TagKey<Item> STORAGE_BLOCKS_RAW_EMBERSTONE = tag("storage_blocks/raw_emberstone");
		public static final TagKey<Item> STORAGE_BLOCKS_LIMONITE = tag("storage_blocks/limonite");
		public static final TagKey<Item> STORAGE_BLOCKS_LUNAR = tag("storage_blocks/lunar");
		public static final TagKey<Item> STORAGE_BLOCKS_LYON = tag("storage_blocks/lyon");
		public static final TagKey<Item> STORAGE_BLOCKS_MYSTITE = tag("storage_blocks/mystite");
		public static final TagKey<Item> STORAGE_BLOCKS_ORNAMYTE = tag("storage_blocks/ornamyte");
		public static final TagKey<Item> STORAGE_BLOCKS_SHYREGEM = tag("storage_blocks/shyregem");
		public static final TagKey<Item> STORAGE_BLOCKS_SHYRESTONE = tag("storage_blocks/shyrestone");
		public static final TagKey<Item> STORAGE_BLOCKS_SKELETAL = tag("storage_blocks/skeletal");
		public static final TagKey<Item> STORAGE_BLOCKS_VARSIUM = tag("storage_blocks/varsium");

		public static final TagKey<Item> GEMS_BLOODSTONE = tag("gems/bloodstone");
		public static final TagKey<Item> GEMS_CRYSTALLITE = tag("gems/crystallite");
		public static final TagKey<Item> GEMS_GEMENYTE = tag("gems/gemenyte");
		public static final TagKey<Item> GEMS_JADE = tag("gems/jade");
		public static final TagKey<Item> GEMS_JEWELYTE = tag("gems/jewelyte");
		public static final TagKey<Item> GEMS_ORNAMYTE = tag("gems/ornamyte");
		public static final TagKey<Item> GEMS_SHYREGEM = tag("gems/shyregem");
		
		public static final TagKey<Item> INGOTS_BARONYTE = tag("ingots/baronyte");
		public static final TagKey<Item> INGOTS_BLAZIUM = tag("ingots/blazium");
		public static final TagKey<Item> INGOTS_ELECANIUM = tag("ingots/elecanium");
		public static final TagKey<Item> INGOTS_EMBERSTONE = tag("ingots/emberstone");
		public static final TagKey<Item> INGOTS_GHASTLY = tag("ingots/ghastly");
		public static final TagKey<Item> INGOTS_GHOULISH = tag("ingots/ghoulish");
		public static final TagKey<Item> INGOTS_LIMONITE = tag("ingots/limonite");
		public static final TagKey<Item> INGOTS_LUNAR = tag("ingots/lunar");
		public static final TagKey<Item> INGOTS_LYON = tag("ingots/lyon");
		public static final TagKey<Item> INGOTS_MYSTITE = tag("ingots/mystite");
		public static final TagKey<Item> INGOTS_SHYRESTONE = tag("ingots/shyrestone");
		public static final TagKey<Item> INGOTS_SKELETAL = tag("ingots/skeletal");
		public static final TagKey<Item> INGOTS_VARSIUM = tag("ingots/varsium");

		public static final TagKey<Item> RAW_MATERIALS_LIMONITE = tag("raw_materials/limonite");
		public static final TagKey<Item> RAW_MATERIALS_EMBERSTONE = tag("raw_materials/emberstone");

		public static final TagKey<Item> NUGGETS_BARONYTE = tag("nuggets/baronyte");
		public static final TagKey<Item> NUGGETS_BLAZIUM = tag("nuggets/blazium");
		public static final TagKey<Item> NUGGETS_ELECANIUM = tag("nuggets/elecanium");
		public static final TagKey<Item> NUGGETS_EMBERSTONE = tag("nuggets/emberstone");
		public static final TagKey<Item> NUGGETS_GHASTLY = tag("nuggets/ghastly");
		public static final TagKey<Item> NUGGETS_GHOULISH = tag("nuggets/ghoulish");
		public static final TagKey<Item> NUGGETS_LIMONITE = tag("nuggets/limonite");
		public static final TagKey<Item> NUGGETS_LUNAR = tag("nuggets/lunar");
		public static final TagKey<Item> NUGGETS_LYON = tag("nuggets/lyon");
		public static final TagKey<Item> NUGGETS_MYSTITE = tag("nuggets/mystite");
		public static final TagKey<Item> NUGGETS_SHYRESTONE = tag("nuggets/shyrestone");
		public static final TagKey<Item> NUGGETS_SKELETAL = tag("nuggets/skeletal");
		public static final TagKey<Item> NUGGETS_VARSIUM = tag("nuggets/varsium");
		
		public static final TagKey<Item> PRECASIAN_BONE = aoaTag("precasian_bone");
		public static final TagKey<Item> ADVENT_RUNE = aoaTag("rune");
		public static final TagKey<Item> FRAME_BENCH_FRAME = aoaTag("frame_bench_frame");
		public static final TagKey<Item> ENERGY_STONE = aoaTag("energy_stone");
		public static final TagKey<Item> SKILL_CRYSTAL = aoaTag("skill_crystal");
		public static final TagKey<Item> HAULING_FISH = aoaTag("hauling_fish");
		public static final TagKey<Item> FAUNAMANCER_TOOL = aoaTag("faunamancer_tool");
		public static final TagKey<Item> POWER_STONE = aoaTag("power_stones");

		public static final TagKey<Item> CURRENCY = tag("currency");
		public static final TagKey<Item> FRUIT = tag("fruit");
		public static final TagKey<Item> MILK = tag("milk");
		public static final TagKey<Item> GINGERBREAD = tag("gingerbread");
		public static final TagKey<Item> MINTS = tag("mints");
		public static final TagKey<Item> FOOD = tag("food");
		public static final TagKey<Item> SHULKER_BOXES = tag("shulker_boxes");
		public static final TagKey<Item> IVORY = tag("ivory");
		public static final TagKey<Item> RODS_METAL = tag("rods/metal");
		public static final TagKey<Item> AIRTIGHT = tag("armor/airtight");

		public static final TagKey<Item> COMPASS_RUNE_CATALYST = aoaTag("compass_rune_catalyst");
		public static final TagKey<Item> DISTORTION_RUNE_CATALYST = aoaTag("distortion_rune_catalyst");
		public static final TagKey<Item> ENERGY_RUNE_CATALYST = aoaTag("energy_rune_catalyst");
		public static final TagKey<Item> FIRE_RUNE_CATALYST = aoaTag("fire_rune_catalyst");
		public static final TagKey<Item> KINETIC_RUNE_CATALYST = aoaTag("kinetic_rune_catalyst");
		public static final TagKey<Item> LIFE_RUNE_CATALYST = aoaTag("life_rune_catalyst");
		public static final TagKey<Item> LUNAR_RUNE_CATALYST = aoaTag("lunar_rune_catalyst");
		public static final TagKey<Item> POISON_RUNE_CATALYST = aoaTag("poison_rune_catalyst");
		public static final TagKey<Item> POWER_RUNE_CATALYST = aoaTag("power_rune_catalyst");
		public static final TagKey<Item> STORM_RUNE_CATALYST = aoaTag("storm_rune_catalyst");
		public static final TagKey<Item> STRIKE_RUNE_CATALYST = aoaTag("strike_rune_catalyst");
		public static final TagKey<Item> WATER_RUNE_CATALYST = aoaTag("water_rune_catalyst");
		public static final TagKey<Item> WITHER_RUNE_CATALYST = aoaTag("wither_rune_catalyst");
		public static final TagKey<Item> WIND_RUNE_CATALYST = aoaTag("wind_rune_catalyst");

		public static final TagKey<Item> STAVES = aoaTag("staves");
		public static final TagKey<Item> GUNS = aoaTag("guns");
		public static final TagKey<Item> MAULS = aoaTag("mauls");
		public static final TagKey<Item> ONE_HANDED_GUNS = aoaTag("one_handed_guns");
		public static final TagKey<Item> SHOTGUNS = aoaTag("shotguns");
		public static final TagKey<Item> BLASTERS = aoaTag("blasters");
		public static final TagKey<Item> GREATBLADES = aoaTag("greatblades");
		public static final TagKey<Item> BULLET_FIRING_GUNS = aoaTag("bullet_firing_guns");

		public static final TagKey<Item> STAVES_ENCHANTABLE = aoaTag("staves_enchantable");
		public static final TagKey<Item> GUNS_ENCHANTABLE = aoaTag("guns_enchantable");
		public static final TagKey<Item> SMALL_GUNS_ENCHANTABLE = aoaTag("small_guns_enchantable");
		public static final TagKey<Item> SHOTGUNS_ENCHANTABLE = aoaTag("shotguns_enchantable");
		public static final TagKey<Item> BLASTERS_ENCHANTABLE = aoaTag("blasters_enchantable");
		public static final TagKey<Item> GREATBLADES_ENCHANTABLE = aoaTag("greatblades_enchantable");
		public static final TagKey<Item> BULLET_FIRING_ENCHANTABLE = aoaTag("bullet_firing_enchantable");
		public static final TagKey<Item> GREED_ENCHANTABLE = aoaTag("greed_enchantable");

		public static final TagKey<Item> DEINOTHERIUM_FOOD = aoaTag("deinotherium_food");
		public static final TagKey<Item> HORNDRON_FOOD = aoaTag("horndron_food");
		public static final TagKey<Item> OPTERYX_FOOD = aoaTag("opteryx_food");

		public static final TagKey<Item> INTERVENTION_ENCHANTMENT_COMPATIBLE = aoaTag("intervention_enchantment_compatible");

		private static TagKey<Item> aoaTag(String id) {
			return ItemTags.create(AdventOfAscension.id(id));
		}

		private static TagKey<Item> tag(String id) {
			return ItemTags.create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}
	}

	public static class Fluids {
		public static final TagKey<Fluid> CANDIED_WATER = tag("candied_water");
		public static final TagKey<Fluid> TOXIC_WASTE = tag("toxic_waste");
		public static final TagKey<Fluid> TAR = tag("tar");

		private static TagKey<Fluid> aoaTag(String id) {
			return FluidTags.create(AdventOfAscension.id(id));
		}

		private static TagKey<Fluid> tag(String id) {
			return FluidTags.create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}
	}

	public static class Entities {
		public static final TagKey<EntityType<?>> FLYING = tag("flying");

		private static TagKey<EntityType<?>> aoaTag(String id) {
			return create(AdventOfAscension.id(id));
		}

		private static TagKey<EntityType<?>> tag(String id) {
			return create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}

		public static TagKey<EntityType<?>> create(ResourceLocation id) {
			return TagKey.create(Registries.ENTITY_TYPE, id);
		}
	}

	public static class Biomes {
		public static final TagKey<Biome> NO_MOB_SPAWNS = tag("no_mob_spawns");
		public static final TagKey<Biome> IS_ABYSS = aoaTag("is_abyss");
		public static final TagKey<Biome> IS_BARATHOS = aoaTag("is_barathos");
		public static final TagKey<Biome> IS_CELEVE = aoaTag("is_celeve");
		public static final TagKey<Biome> IS_CRYSTEVIA = aoaTag("is_crystevia");
		public static final TagKey<Biome> IS_DEEPLANDS = aoaTag("is_deeplands");
		public static final TagKey<Biome> IS_DUSTOPIA = aoaTag("is_dustopia");
		public static final TagKey<Biome> IS_LBOREAN = aoaTag("is_lborean");
		public static final TagKey<Biome> IS_LELYETIA = aoaTag("is_lelyetia");
		public static final TagKey<Biome> IS_NOWHERE = aoaTag("is_nowhere");
		public static final TagKey<Biome> IS_PRECASIA = aoaTag("is_precasia");
		public static final TagKey<Biome> HAS_RUINED_TELEPORTER = aoaTag("has_structure/ruined_teleporter");
		public static final TagKey<Biome> HAS_PRECASIAN_LOTTO_HOVEL = aoaTag("has_structure/precasian_lotto_hovel");
		public static final TagKey<Biome> HAS_ATTERCOPUS_NEST = aoaTag("has_structure/attercopus_nest");

		private static TagKey<Biome> aoaTag(String id) {
			return create(AdventOfAscension.id(id));
		}

		private static TagKey<Biome> tag(String id) {
			return create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}

		public static TagKey<Biome> create(ResourceLocation id) {
			return TagKey.create(Registries.BIOME, id);
		}
	}

	public static class Structures {
		public static final TagKey<Structure> ON_RUINED_TELEPORTER_FRAME_MAPS = aoaTag("on_ruined_teleporter_frame_maps");

		private static TagKey<Structure> aoaTag(String id) {
			return create(AdventOfAscension.id(id));
		}

		private static TagKey<Structure> tag(String id) {
			return create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}

		public static TagKey<Structure> create(ResourceLocation id) {
			return TagKey.create(Registries.STRUCTURE, id);
		}
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> GUN = aoaTag("gun");
		public static final TagKey<DamageType> ENERGY = aoaTag("energy");
		public static final TagKey<DamageType> NO_SPIRIT_REGEN = aoaTag("no_spirit_regen");
		public static final TagKey<DamageType> REDUCED_FLINCH = tag("reduced_flinch");

		private static TagKey<DamageType> aoaTag(String id) {
			return create(AdventOfAscension.id(id));
		}

		private static TagKey<DamageType> tag(String id) {
			return create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}

		public static TagKey<DamageType> create(ResourceLocation id) {
			return TagKey.create(Registries.DAMAGE_TYPE, id);
		}
	}

	public static class BannerPatterns {
		public static final TagKey<BannerPattern> COMPASS_RUNE = aoaTag("pattern_item/compass_rune");
		public static final TagKey<BannerPattern> DISTORTION_RUNE = aoaTag("pattern_item/distortion_rune");
		public static final TagKey<BannerPattern> ENERGY_RUNE = aoaTag("pattern_item/energy_rune");
		public static final TagKey<BannerPattern> FIRE_RUNE = aoaTag("pattern_item/fire_rune");
		public static final TagKey<BannerPattern> KINETIC_RUNE = aoaTag("pattern_item/kinetic_rune");
		public static final TagKey<BannerPattern> LIFE_RUNE = aoaTag("pattern_item/life_rune");
		public static final TagKey<BannerPattern> LUNAR_RUNE = aoaTag("pattern_item/lunar_rune");
		public static final TagKey<BannerPattern> POISON_RUNE = aoaTag("pattern_item/poison_rune");
		public static final TagKey<BannerPattern> POWER_RUNE = aoaTag("pattern_item/power_rune");
		public static final TagKey<BannerPattern> STORM_RUNE = aoaTag("pattern_item/storm_rune");
		public static final TagKey<BannerPattern> STRIKE_RUNE = aoaTag("pattern_item/strike_rune");
		public static final TagKey<BannerPattern> WATER_RUNE = aoaTag("pattern_item/water_rune");
		public static final TagKey<BannerPattern> WIND_RUNE = aoaTag("pattern_item/wind_rune");
		public static final TagKey<BannerPattern> WITHER_RUNE = aoaTag("pattern_item/wither_rune");

		private static TagKey<BannerPattern> aoaTag(String id) {
			return create(AdventOfAscension.id(id));
		}

		private static TagKey<BannerPattern> tag(String id) {
			return create(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, id));
		}

		public static TagKey<BannerPattern> create(ResourceLocation id) {
			return TagKey.create(Registries.BANNER_PATTERN, id);
		}
	}
}
