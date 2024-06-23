package net.tslat.aoa3.common.registration.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
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

	public static final DeferredItem<EmberstonePickaxe> EMBERSTONE_PICKAXE = registerTool("emberstone_pickaxe", () -> new EmberstonePickaxe(AoATiers.EMBERSTONE, BasePickaxe.baseProperties(AoATiers.EMBERSTONE, -4.5f, AttackSpeed.PICKAXE)));
	public static final DeferredItem<EnergisticPickaxe> ENERGISTIC_PICKAXE = registerTool("energistic_pickaxe", () -> new EnergisticPickaxe(AoATiers.ENERGISTIC, BasePickaxe.baseProperties(AoATiers.ENERGISTIC, -2, AttackSpeed.PICKAXE).component(AoADataComponents.CHARGE, 0f)));
	public static final DeferredItem<Gemcracker> GEMCRACKER = registerTool("gemcracker", () -> new Gemcracker(AoATiers.GEMCRACKER, BasePickaxe.baseProperties(AoATiers.GEMCRACKER, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<GoofyPickaxe> GOOFY_PICKAXE = registerTool("goofy_pickaxe", () -> new GoofyPickaxe(AoATiers.GOOFY, BasePickaxe.baseProperties(AoATiers.GOOFY)));
	public static final DeferredItem<BasePickaxe> JADE_PICKAXE = registerTool("jade_pickaxe", () -> new BasePickaxe(AoATiers.JADE, BasePickaxe.baseProperties(AoATiers.JADE, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<BasePickaxe> LIMONITE_PICKAXE = registerTool("limonite_pickaxe", () -> new BasePickaxe(AoATiers.LIMONITE, BasePickaxe.baseProperties(AoATiers.LIMONITE, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<OccultPickaxe> OCCULT_PICKAXE = registerTool("occult_pickaxe", () -> new OccultPickaxe(AoATiers.OCCULT, BasePickaxe.baseProperties(AoATiers.OCCULT, -2, AttackSpeed.PICKAXE).rarity(Rarity.RARE)));
	public static final DeferredItem<OrnamytePickaxe> ORNAMYTE_PICKAXE = registerTool("ornamyte_pickaxe", () -> new OrnamytePickaxe(AoATiers.ORNAMYTE, BasePickaxe.baseProperties(AoATiers.ORNAMYTE, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<Pickmax> PICKMAX = registerTool("pickmax", () -> new Pickmax(AoATiers.PICKMAX, BasePickaxe.baseProperties(AoATiers.PICKMAX, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<SkeletalPickaxe> SKELETAL_PICKAXE = registerTool("skeletal_pickaxe", () -> new SkeletalPickaxe(AoATiers.SKELETAL, BasePickaxe.baseProperties(AoATiers.SKELETAL, -2, AttackSpeed.PICKAXE)));
	public static final DeferredItem<SoulstonePickaxe> SOULSTONE_PICKAXE = registerTool("soulstone_pickaxe", () -> new SoulstonePickaxe(AoATiers.SOULSTONE, BasePickaxe.baseProperties(AoATiers.SOULSTONE, -2, AttackSpeed.PICKAXE)));

	public static final DeferredItem<BaseShovel> EMBERSTONE_SHOVEL = registerTool("emberstone_shovel", () -> new EmberstoneShovel(AoATiers.EMBERSTONE, BaseShovel.baseProperties(AoATiers.EMBERSTONE, -4f, AttackSpeed.SHOVEL)));
	public static final DeferredItem<BaseShovel> ENERGISTIC_SHOVEL = registerTool("energistic_shovel", () -> new EnergisticShovel(AoATiers.ENERGISTIC, BaseShovel.baseProperties(AoATiers.ENERGISTIC, -3.5f, AttackSpeed.SHOVEL).component(AoADataComponents.CHARGE, 0f)));
	public static final DeferredItem<BaseShovel> GOOFY_SHOVEL = registerTool("goofy_shovel", () -> new GoofyShovel(AoATiers.GOOFY, BaseShovel.baseProperties(AoATiers.GOOFY)));
	public static final DeferredItem<BaseShovel> JADE_SHOVEL = registerTool("jade_shovel", () -> new BaseShovel(AoATiers.JADE, BaseShovel.baseProperties(AoATiers.JADE, -1.5f, AttackSpeed.SHOVEL)));
	public static final DeferredItem<BaseShovel> LIMONITE_SHOVEL = registerTool("limonite_shovel", () -> new BaseShovel(AoATiers.LIMONITE, BaseShovel.baseProperties(AoATiers.LIMONITE, -1.5f, AttackSpeed.SHOVEL)));
	public static final DeferredItem<BaseShovel> OCCULT_SHOVEL = registerTool("occult_shovel", () -> new OccultShovel(AoATiers.OCCULT, BaseShovel.baseProperties(AoATiers.OCCULT, -3.5f, AttackSpeed.SHOVEL).rarity(Rarity.RARE)));
	public static final DeferredItem<BaseShovel> ORNAMYTE_SHOVEL = registerTool("ornamyte_shovel", () -> new OrnamyteShovel(AoATiers.ORNAMYTE, BaseShovel.baseProperties(AoATiers.ORNAMYTE, -1.5f, AttackSpeed.SHOVEL)));
	public static final DeferredItem<BaseShovel> SKELETAL_SHOVEL = registerTool("skeletal_shovel", () -> new SkeletalShovel(AoATiers.SKELETAL, BaseShovel.baseProperties(AoATiers.SKELETAL, -1.5f, AttackSpeed.SHOVEL)));
	public static final DeferredItem<BaseShovel> SOULSTONE_SHOVEL = registerTool("soulstone_shovel", () -> new SoulstoneShovel(AoATiers.SOULSTONE, BaseShovel.baseProperties(AoATiers.SOULSTONE, -3.5f, AttackSpeed.SHOVEL)));

	public static final DeferredItem<BaseAxe> EMBERSTONE_AXE = registerTool("emberstone_axe", () -> new EmberstoneAxe(AoATiers.EMBERSTONE, BaseAxe.baseProperties(AoATiers.EMBERSTONE, 1.5f, AttackSpeed.AXE)));
	public static final DeferredItem<BaseAxe> ENERGISTIC_AXE = registerTool("energistic_axe", () -> new EnergisticAxe(AoATiers.ENERGISTIC, BaseAxe.baseProperties(AoATiers.ENERGISTIC, 2f, AttackSpeed.AXE).component(AoADataComponents.CHARGE, 0f)));
	public static final DeferredItem<BaseAxe> GOOFY_AXE = registerTool("goofy_axe", () -> new GoofyAxe(AoATiers.GOOFY, BaseAxe.baseProperties(AoATiers.GOOFY)));
	public static final DeferredItem<BaseAxe> JADE_AXE = registerTool("jade_axe", () -> new BaseAxe(AoATiers.JADE, BaseShovel.baseProperties(AoATiers.JADE, 2f, AttackSpeed.AXE)));
	public static final DeferredItem<BaseAxe> LIMONITE_AXE = registerTool("limonite_axe", () -> new BaseAxe(AoATiers.LIMONITE, BaseShovel.baseProperties(AoATiers.LIMONITE, 2.5f, AttackSpeed.forAttacksPerSecond(0.95f))));
	public static final DeferredItem<BaseAxe> OCCULT_AXE = registerTool("occult_axe", () -> new OccultAxe(AoATiers.OCCULT, BaseAxe.baseProperties(AoATiers.OCCULT, 2f, AttackSpeed.AXE).rarity(Rarity.RARE)));
	public static final DeferredItem<BaseAxe> ORNAMYTE_AXE = registerTool("ornamyte_axe", () -> new OrnamyteAxe(AoATiers.ORNAMYTE, BaseAxe.baseProperties(AoATiers.ORNAMYTE, 4f, AttackSpeed.AXE)));
	public static final DeferredItem<BaseAxe> SKELETAL_AXE = registerTool("skeletal_axe", () -> new SkeletalAxe(AoATiers.SKELETAL, BaseAxe.baseProperties(AoATiers.SKELETAL, 3.5f, AttackSpeed.AXE)));
	public static final DeferredItem<BaseAxe> SOULSTONE_AXE = registerTool("soulstone_axe", () -> new SoulstoneAxe(AoATiers.SOULSTONE, BaseAxe.baseProperties(AoATiers.SOULSTONE, 1.5f, AttackSpeed.AXE)));
	public static final DeferredItem<Chainsaw> CHAINSAW = registerTool("chainsaw", () -> new Chainsaw(AoATiers.CHAINSAW, BaseAxe.baseProperties(AoATiers.CHAINSAW)));

	public static final DeferredItem<DryadsBlessing> DRYADS_BLESSING = registerTool("dryads_blessing", DryadsBlessing::new);
	public static final DeferredItem<BaseHoe> LIMONITE_HOE = registerTool("limonite_hoe", () -> new BaseHoe(AoATiers.LIMONITE, -6.5f, AttackSpeed.forAttacksPerSecond(3)));
	public static final DeferredItem<BaseHoe> JADE_HOE = registerTool("jade_hoe", () -> new BaseHoe(AoATiers.JADE, -7.5f, AttackSpeed.forAttacksPerSecond(4)));
	public static final DeferredItem<BaseHoe> EMBERSTONE_HOE = registerTool("emberstone_hoe", () -> new BaseHoe(AoATiers.EMBERSTONE, -12f, AttackSpeed.forAttacksPerSecond(4)));

	public static final DeferredItem<Item> HAULING_ROD = registerTool("hauling_rod", () -> new HaulingRod(new Item.Properties().durability(400)));
	public static final DeferredItem<Item> THERMALLY_INSULATED_ROD = registerTool("thermally_insulated_rod", () -> new ThermallyInsulatedRod(new Item.Properties().durability(400)));
	public static final DeferredItem<Item> LIGHT_ROD = registerTool("light_rod", () -> new LightRod(new Item.Properties().durability(160)));
	public static final DeferredItem<Item> GOLDEN_ROD = registerTool("golden_rod", () -> new GoldenRod(new Item.Properties().durability(280).rarity(Rarity.EPIC)));
	public static final DeferredItem<Item> FISHING_CAGE = registerTool("fishing_cage", () -> new FishingCage(new Item.Properties().durability(10)));

	public static final DeferredItem<Item> ATTUNING_BOWL = registerTool("attuning_bowl", AttuningBowl::new); // TODO Retexture
	public static final DeferredItem<Item> EXP_FLASK = registerTool("exp_flask", ExpFlask::new);
	public static final DeferredItem<Item> GRAVITATOR = registerTool("gravitator", Gravitator::new);
	public static final DeferredItem<Item> DISTORTING_ARTIFACT = registerTool("distorting_artifact", DistortingArtifact::new);

	private static <T extends Item> DeferredItem<T> registerTool(String registryName, Supplier<T> item) {
		return AoAItems.registerItem(registryName, item, AoACreativeModeTabs.TOOLS.getKey());
	}
}
