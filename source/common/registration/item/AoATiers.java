package net.tslat.aoa3.common.registration.item;

import net.minecraft.world.item.Tier;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.library.builder.ToolTierBuilder;

public final class AoATiers {
	public static final Tier EMBERSTONE = ToolTierBuilder.tool().baseSpeed(10f).durability(2050).damage(12f).enchantValue(12).repairsWith(AoATags.Items.INGOTS_EMBERSTONE).doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_EMBERSTONE_TOOL);
	public static final Tier JADE = ToolTierBuilder.tool().baseSpeed(8.5f).durability(950).damage(7.5f).enchantValue(12).repairsWith(AoATags.Items.GEMS_JADE).doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_JADE_TOOL);
	public static final Tier LIMONITE = ToolTierBuilder.tool().baseSpeed(7f).durability(176).damage(6.5f).enchantValue(3).repairsWith(AoATags.Items.INGOTS_LIMONITE).doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_LIMONITE_TOOL);
	public static final Tier ORNAMYTE = ToolTierBuilder.tool().baseSpeed(10f).durability(2110).damage(10f).enchantValue(10).repairsWith(AoATags.Items.GEMS_ORNAMYTE).doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_ORNAMYTE_TOOL);
	public static final Tier SKELETAL = ToolTierBuilder.tool().baseSpeed(10f).durability(1900).damage(10f).enchantValue(12).repairsWith(AoATags.Items.INGOTS_SKELETAL).doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_SKELETAL_TOOL);

	public static final Tier ENERGISTIC = ToolTierBuilder.tool().baseSpeed(11).durability(2600).damage(12f).enchantValue(13).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_ENERGISTIC_TOOL);
	public static final Tier GEMCRACKER = ToolTierBuilder.tool().baseSpeed(10).durability(2350).damage(11f).enchantValue(11).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_GEMCRACKER_TOOL);
	public static final Tier GOOFY = ToolTierBuilder.tool().baseSpeed(8).durability(1500).damage(0).enchantValue(15).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_GOOFY_TOOL);
	public static final Tier OCCULT = ToolTierBuilder.tool().baseSpeed(11f).durability(2650).damage(12f).enchantValue(15).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_OCCULT_TOOL);
	public static final Tier PICKMAX = ToolTierBuilder.tool().baseSpeed(8).durability(3000).damage(12f).enchantValue(14).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_PICKMAX_TOOL);
	public static final Tier SOULSTONE = ToolTierBuilder.tool().baseSpeed(11).durability(2570).damage(12f).enchantValue(12).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_SOULSTONE_TOOL);

	public static final Tier CHAINSAW = ToolTierBuilder.tool().baseSpeed(18).durability(2500).damage(9f).enchantValue(0).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_CHAINSAW_TOOL);
	public static final Tier DRYADS_BLESSING = ToolTierBuilder.tool().baseSpeed(10).durability(3020).damage(0).enchantValue(18).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_DRYADS_BLESSING_TOOL);

	public static final Tier BARON = ToolTierBuilder.weapon().durability(1975).damage(12f).enchantValue(15).repairsWith(AoATags.Items.INGOTS_BARONYTE).isNonHarvesting();
	public static final Tier BLOODFURY = ToolTierBuilder.weapon().durability(1990).damage(13f).enchantValue(11).noRepair().isNonHarvesting();
	public static final Tier BLOODSTONE = ToolTierBuilder.weapon().durability(1990).damage(15f).enchantValue(11).repairsWith(AoATags.Items.GEMS_BLOODSTONE).isNonHarvesting();
	public static final Tier CANDLEFIRE = ToolTierBuilder.weapon().durability(2985).damage(18f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier CARAMEL_CARVER = ToolTierBuilder.weapon().durability(2400).damage(16.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier CORALSTORM = ToolTierBuilder.weapon().durability(1800).damage(9f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier CREEPIFIED = ToolTierBuilder.weapon().durability(2090).damage(14.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier CRYSTALLITE = ToolTierBuilder.weapon().durability(2310).damage(15f).enchantValue(12).repairsWith(AoATags.Items.GEMS_CRYSTALLITE).isNonHarvesting();
	public static final Tier EXPLOCHRON = ToolTierBuilder.weapon().durability(2360).damage(16f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier FIREBORNE = ToolTierBuilder.weapon().durability(1995).damage(13f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier GUARDIAN = ToolTierBuilder.weapon().durability(2420).damage(16f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier HARVESTER = ToolTierBuilder.weapon().durability(2550).damage(18.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier HOLY = ToolTierBuilder.weapon().durability(2500).damage(1).enchantValue(18).noRepair().isNonHarvesting();
	public static final Tier ILLUSION = ToolTierBuilder.weapon().durability(2300).damage(15.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier LEGBONE = ToolTierBuilder.weapon().durability(2020).damage(12.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier LIGHTS_WAY = ToolTierBuilder.weapon().durability(2900).damage(8.5f).enchantValue(12).repairsWith(AoATags.Items.INGOTS_SHYRESTONE).isNonHarvesting();
	public static final Tier NETHENGEIC = ToolTierBuilder.weapon().durability(2120).damage(15f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier PRIMAL = ToolTierBuilder.weapon().durability(2060).damage(14f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier ROCKBASHER = ToolTierBuilder.weapon().durability(2430).damage(17f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier ROCK_PICK = ToolTierBuilder.tool().baseSpeed(9.5f).durability(2100).damage(13f).enchantValue(15).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_ROCK_PICK_TOOL);
	public static final Tier ROSIDIAN = ToolTierBuilder.weapon().durability(2430).damage(16.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier RUNIC = ToolTierBuilder.weapon().durability(2690).damage(18.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier SHADOW = ToolTierBuilder.weapon().durability(2585).damage(18f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier SHROOMUS = ToolTierBuilder.weapon().durability(2295).damage(16f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier SUPREMACY = ToolTierBuilder.weapon().durability(2270).damage(15.5f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier SWEET = ToolTierBuilder.weapon().durability(2360).damage(16f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier TROLL_BASHER = ToolTierBuilder.tool().baseSpeed(9.5f).durability(1800).damage(13f).enchantValue(15).noRepair().doesntHarvestBlocks(AoATags.Blocks.INCORRECT_FOR_TROLL_BASHER_TOOL);
	public static final Tier ULTRAFLAME = ToolTierBuilder.weapon().durability(2350).damage(16f).enchantValue(15).noRepair().isNonHarvesting();
	public static final Tier VOID = ToolTierBuilder.weapon().durability(1750).damage(11.5f).enchantValue(11).noRepair().isNonHarvesting();

	public static final Tier FAUNAMANCERS_BLADE = ToolTierBuilder.weapon().durability(280).damage(4).enchantValue(20).noRepair().isNonHarvesting();

	public static final Tier BARON_GREATBLADE = ToolTierBuilder.weapon().durability(1200).damage(20.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier BLOODLURKER_GREATBLADE = ToolTierBuilder.weapon().durability(1350).damage(22f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier CANDY_BLADE_GREATBLADE = ToolTierBuilder.weapon().durability(1450).damage(24f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier CORAL_GREATBLADE = ToolTierBuilder.weapon().durability(1800).damage(25.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier COTTON_CRUSHER_GREATBLADE = ToolTierBuilder.weapon().durability(1600).damage(25.0f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier CREEPOID_GREATBLADE = ToolTierBuilder.weapon().durability(1080).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier CRYSTAL_GREATBLADE = ToolTierBuilder.weapon().durability(1480).damage(23f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier EREBON_SCYTHE = ToolTierBuilder.weapon().durability(1750).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier GODS_GREATBLADE = ToolTierBuilder.weapon().durability(2000).damage(30.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier GOOFY_GREATBLADE = ToolTierBuilder.weapon().durability(1300).damage(23.0f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier HAUNTED_GREATBLADE = ToolTierBuilder.weapon().durability(1875).damage(27f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier KNIGHTS_GUARD_GREATBLADE = ToolTierBuilder.weapon().durability(2050).damage(27.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier LELYETIAN_GREATBLADE = ToolTierBuilder.weapon().durability(1100).damage(19.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier LUNAR_GREATBLADE = ToolTierBuilder.weapon().durability(1850).damage(13.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier LUXON_SCYTHE = ToolTierBuilder.weapon().durability(1750).damage(18.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier LYONIC_GREATBLADE = ToolTierBuilder.weapon().durability(1420).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier MILLENNIUM_GREATBLADE = ToolTierBuilder.weapon().durability(2050).damage(14.25f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier NOXIOUS_GREATBLADE = ToolTierBuilder.weapon().durability(1580).damage(24f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier PLUTON_SCYTHE = ToolTierBuilder.weapon().durability(1750).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier PRIMORDIAL_GREATBLADE = ToolTierBuilder.weapon().durability(1900).damage(26.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier ROSIDIAN_GREATBLADE = ToolTierBuilder.weapon().durability(1470).damage(23.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier ROYAL_GREATBLADE = ToolTierBuilder.weapon().durability(1130).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier RUNIC_GREATBLADE = ToolTierBuilder.weapon().durability(1800).damage(25.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier SELYAN_SCYTHE = ToolTierBuilder.weapon().durability(1750).damage(20f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier SHROOMIC_GREATBLADE = ToolTierBuilder.weapon().durability(1300).damage(22.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier SHYRE_SWORD_GREATBLADE = ToolTierBuilder.weapon().durability(2000).damage(27f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier SUBTERRANEAN_GREATBLADE = ToolTierBuilder.weapon().durability(1160).damage(22.5f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier TIDAL_GREATBLADE = ToolTierBuilder.weapon().durability(1750).damage(25f).enchantValue(13).noRepair().isNonHarvesting();
	public static final Tier UNDERWORLD_GREATBLADE = ToolTierBuilder.weapon().durability(1050).damage(19.5f).enchantValue(13).noRepair().isNonHarvesting();
}
