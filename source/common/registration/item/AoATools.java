package net.tslat.aoa3.common.registration.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoACreativeModeTabs;
import net.tslat.aoa3.content.item.misc.DistortingArtifact;
import net.tslat.aoa3.content.item.misc.Gravitator;
import net.tslat.aoa3.content.item.tool.axe.*;
import net.tslat.aoa3.content.item.tool.hoe.BaseHoe;
import net.tslat.aoa3.content.item.tool.hoe.DryadsBlessing;
import net.tslat.aoa3.content.item.tool.misc.*;
import net.tslat.aoa3.content.item.tool.pickaxe.*;
import net.tslat.aoa3.content.item.tool.shovel.*;
import net.tslat.aoa3.library.constant.AttackSpeed;

import java.util.function.Supplier;

public final class AoATools {
	public static void init() {}

	public static final DeferredHolder<Item, BasePickaxe> EMBERSTONE_PICKAXE = registerTool("emberstone_pickaxe", EmberstonePickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> ENERGISTIC_PICKAXE = registerTool("energistic_pickaxe", EnergisticPickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> GEMCRACKER = registerTool("gemcracker", Gemcracker::new);
	public static final DeferredHolder<Item, BasePickaxe> GOOFY_PICKAXE = registerTool("goofy_pickaxe", GoofyPickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> JADE_PICKAXE = registerTool("jade_pickaxe", () -> new BasePickaxe(AoATiers.JADE, -2, AttackSpeed.PICKAXE));
	public static final DeferredHolder<Item, BasePickaxe> LIMONITE_PICKAXE = registerTool("limonite_pickaxe", () -> new BasePickaxe(AoATiers.LIMONITE, -2, AttackSpeed.PICKAXE));
	public static final DeferredHolder<Item, BasePickaxe> OCCULT_PICKAXE = registerTool("occult_pickaxe", OccultPickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> ORNAMYTE_PICKAXE = registerTool("ornamyte_pickaxe", OrnamytePickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> PICKMAX = registerTool("pickmax", Pickmax::new);
	public static final DeferredHolder<Item, BasePickaxe> SKELETAL_PICKAXE = registerTool("skeletal_pickaxe", SkeletalPickaxe::new);
	public static final DeferredHolder<Item, BasePickaxe> SOULSTONE_PICKAXE = registerTool("soulstone_pickaxe", SoulstonePickaxe::new);

	public static final DeferredHolder<Item, BaseShovel> EMBERSTONE_SHOVEL = registerTool("emberstone_shovel", EmberstoneShovel::new);
	public static final DeferredHolder<Item, BaseShovel> ENERGISTIC_SHOVEL = registerTool("energistic_shovel", EnergisticShovel::new);
	public static final DeferredHolder<Item, BaseShovel> GOOFY_SHOVEL = registerTool("goofy_shovel", GoofyShovel::new);
	public static final DeferredHolder<Item, BaseShovel> JADE_SHOVEL = registerTool("jade_shovel", () -> new BaseShovel(AoATiers.JADE, -1.5f, AttackSpeed.SHOVEL));
	public static final DeferredHolder<Item, BaseShovel> LIMONITE_SHOVEL = registerTool("limonite_shovel", () -> new BaseShovel(AoATiers.LIMONITE, -1.5f, AttackSpeed.SHOVEL));
	public static final DeferredHolder<Item, BaseShovel> OCCULT_SHOVEL = registerTool("occult_shovel", OccultShovel::new);
	public static final DeferredHolder<Item, BaseShovel> ORNAMYTE_SHOVEL = registerTool("ornamyte_shovel", OrnamyteShovel::new);
	public static final DeferredHolder<Item, BaseShovel> SKELETAL_SHOVEL = registerTool("skeletal_shovel", SkeletalShovel::new);
	public static final DeferredHolder<Item, BaseShovel> SOULSTONE_SHOVEL = registerTool("soulstone_shovel", SoulstoneShovel::new);

	public static final DeferredHolder<Item, BaseAxe> EMBERSTONE_AXE = registerTool("emberstone_axe", EmberstoneAxe::new);
	public static final DeferredHolder<Item, BaseAxe> ENERGISTIC_AXE = registerTool("energistic_axe", EnergisticAxe::new);
	public static final DeferredHolder<Item, BaseAxe> GOOFY_AXE = registerTool("goofy_axe", GoofyAxe::new);
	public static final DeferredHolder<Item, BaseAxe> JADE_AXE = registerTool("jade_axe", () -> new BaseAxe(AoATiers.JADE, 2f, AttackSpeed.AXE));
	public static final DeferredHolder<Item, BaseAxe> LIMONITE_AXE = registerTool("limonite_axe", () -> new BaseAxe(AoATiers.LIMONITE, 2.5f, AttackSpeed.forAttacksPerSecond(0.95f)));
	public static final DeferredHolder<Item, BaseAxe> OCCULT_AXE = registerTool("occult_axe", OccultAxe::new);
	public static final DeferredHolder<Item, BaseAxe> ORNAMYTE_AXE = registerTool("ornamyte_axe", OrnamyteAxe::new);
	public static final DeferredHolder<Item, BaseAxe> SKELETAL_AXE = registerTool("skeletal_axe", SkeletalAxe::new);
	public static final DeferredHolder<Item, BaseAxe> SOULSTONE_AXE = registerTool("soulstone_axe", SoulstoneAxe::new);
	public static final DeferredHolder<Item, Chainsaw> CHAINSAW = registerTool("chainsaw", Chainsaw::new);

	public static final DeferredHolder<Item, DryadsBlessing> DRYADS_BLESSING = registerTool("dryads_blessing", DryadsBlessing::new);
	public static final DeferredHolder<Item, BaseHoe> LIMONITE_HOE = registerTool("limonite_hoe", () -> new BaseHoe(AoATiers.LIMONITE.adjusted(AdventOfAscension.id("limonite_hoe")).damage(1f), 0, AttackSpeed.forAttacksPerSecond(3)));
	public static final DeferredHolder<Item, BaseHoe> JADE_HOE = registerTool("jade_hoe", () -> new BaseHoe(AoATiers.JADE.adjusted(AdventOfAscension.id("jade_hoe")).damage(1f), 0, AttackSpeed.forAttacksPerSecond(4)));
	public static final DeferredHolder<Item, BaseHoe> EMBERSTONE_HOE = registerTool("emberstone_hoe", () -> new BaseHoe(AoATiers.EMBERSTONE.adjusted(AdventOfAscension.id("emberstone_hoe")).damage(1f), 0, AttackSpeed.forAttacksPerSecond(4)));

	public static final DeferredHolder<Item, Item> HAULING_ROD = registerTool("hauling_rod", () -> new HaulingRod(new Item.Properties().durability(400)));
	public static final DeferredHolder<Item, Item> THERMALLY_INSULATED_ROD = registerTool("thermally_insulated_rod", () -> new ThermallyInsulatedRod(new Item.Properties().durability(400)));
	public static final DeferredHolder<Item, Item> LIGHT_ROD = registerTool("light_rod", () -> new LightRod(new Item.Properties().durability(160)));
	public static final DeferredHolder<Item, Item> GOLDEN_ROD = registerTool("golden_rod", () -> new GoldenRod(new Item.Properties().durability(280).rarity(Rarity.EPIC)));
	public static final DeferredHolder<Item, Item> FISHING_CAGE = registerTool("fishing_cage", () -> new FishingCage(new Item.Properties().durability(10)));

	public static final DeferredHolder<Item, Item> STONE_BOWL = registerTool("stone_bowl", () -> new InfusionBowl(100, 1, 0));
	public static final DeferredHolder<Item, Item> DIAMOND_BOWL = registerTool("diamond_bowl", () -> new InfusionBowl(750, 5, 10));
	public static final DeferredHolder<Item, Item> EXP_FLASK = registerTool("exp_flask", ExpFlask::new);
	public static final DeferredHolder<Item, Item> GRAVITATOR = registerTool("gravitator", Gravitator::new);
	public static final DeferredHolder<Item, Item> DISTORTING_ARTIFACT = registerTool("distorting_artifact", DistortingArtifact::new);

	private static <T extends Item> DeferredHolder<Item, T> registerTool(String registryName, Supplier<T> item) {
		return AoAItems.registerItem(registryName, item, AoACreativeModeTabs.TOOLS.getKey());
	}
}
