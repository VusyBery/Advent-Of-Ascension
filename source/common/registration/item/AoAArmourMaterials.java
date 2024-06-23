package net.tslat.aoa3.common.registration.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public final class AoAArmourMaterials {
	public static void init() {}

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ACHELOS_HELMET = register("achelos_helmet", Builder.helmet(5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OCEANUS_HELMET = register("oceanus_helmet", Builder.helmet(5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SEALORD_HELMET = register("sealord_helmet", Builder.helmet(5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FACE_MASK = register("face_mask", Builder.helmet(5).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NIGHT_VISION_GOGGLES = register("night_vision_goggles", Builder.helmet(2).toughness(1));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SKILL_HELMET = register("skill_helmet", Builder.helmet(5).toughness(7).enchantValue(20).equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OLD_BOOT = register("old_boot", Builder.boots(2).enchantValue(0).equipSound(SoundEvents.ARMOR_EQUIP_LEATHER));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ALACRITY = register("alacrity", Builder.armour(4, 8, 9, 3).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARCHAIC = register("archaic", Builder.armour(5, 9, 8, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BARON = register("baron", Builder.armour(4, 6, 9, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BATTLEBORN = register("battleborn", Builder.armour(4, 8, 9, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BIOGENIC = register("biogenic", Builder.armour(3, 6, 8, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BOREIC = register("boreic", Builder.armour(4, 8, 10, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CANDY = register("candy", Builder.armour(4, 7, 9, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMMANDER = register("commander", Builder.armour(4, 9, 9, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CRYSTALLIS = register("crystallis", Builder.armour(5, 6, 10, 3).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ELECANYTE = register("elecanyte", Builder.armour(4, 8, 9, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EMBRODIUM = register("embrodium", Builder.armour(4, 7, 8, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXOPLATE = register("exoplate", Builder.armour(4, 6, 8, 4).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EXPLOSIVE = register("explosive", Builder.armour(4, 7, 9, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FUNGAL = register("fungal", Builder.armour(5, 6, 8, 5).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GHASTLY = register("ghastly", Builder.armour(5, 8, 8, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GHOULISH = register("ghoulish", Builder.armour(6, 6, 8, 6).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HAZMAT = register("hazmat", Builder.armour(2, 5, 6, 2).equipSound(SoundEvents.ARMOR_EQUIP_LEATHER));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HYDRANGIC = register("hydrangic", Builder.armour(4, 7, 9, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HYDROPLATE = register("hydroplate", Builder.armour(4, 8, 10, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ICE = register("ice", Builder.armour(2, 6, 6, 2).toughness(2));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> INFERNAL = register("infernal", Builder.armour(4, 7, 8, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KNIGHT = register("knight", Builder.armour(4, 8, 9, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LUNAR = register("lunar", Builder.armour(4, 7, 10, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LYNDAMYTE = register("lyndamyte", Builder.armour(3, 6, 8, 3).toughness(2));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LYONIC = register("lyonic", Builder.armour(4, 7, 8, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MERCURIAL = register("mercurial", Builder.armour(3, 8, 8, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NECRO = register("necro", Builder.armour(5, 8, 9, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NETHENGEIC = register("nethengeic", Builder.armour(3, 7, 8, 4).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NIGHTMARE = register("nightmare", Builder.armour(4, 9, 8, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OMNI = register("omni", Builder.armour(3, 6, 8, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PHANTASM = register("phantasm", Builder.armour(3, 8, 8, 5).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> POISON = register("poison", Builder.armour(5, 6, 9, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PREDATIOUS = register("predatious", Builder.armour(3, 7, 9, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PRIMORDIAL = register("primordial", Builder.armour(5, 8, 9, 4).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PURITY = register("purity", Builder.armour(5, 8, 8, 3).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ROCKBONE = register("rockbone", Builder.armour(3, 7, 9, 3).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ROSIDIAN = register("rosidian", Builder.armour(4, 7, 9, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RUNIC = register("runic", Builder.armour(5, 8, 9, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SHARPSHOT = register("sharpshot", Builder.armour(4, 6, 9, 5).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SKELETAL = register("skeletal", Builder.armour(3, 7, 8, 4).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SPACEKING = register("spaceking", Builder.armour(4, 8, 9, 5).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SPEED = register("speed", Builder.armour(4, 9, 9, 3).toughness(7));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SUBTERRANEAN = register("subterranean", Builder.armour(3, 7, 8, 4).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> UTOPIAN = register("utopian", Builder.armour(3, 6, 8, 3).toughness(2));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> VOID = register("void", Builder.armour(3, 6, 8, 3).toughness(2));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WEAKEN = register("weaken", Builder.armour(4, 6, 8, 4).toughness(3));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WITHER = register("wither", Builder.armour(4, 8, 8, 4).toughness(5));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ZARGONITE = register("zargonite", Builder.armour(5, 8, 9, 4).toughness(7));

	private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String id, Builder builder) {
		final ArmorMaterial.Layer baseLayer = new ArmorMaterial.Layer(AdventOfAscension.id(id));

		if (builder.layers == null) {
			builder.layers = Collections.singletonList(baseLayer);
		}
		else {
			builder.layers.addFirst(baseLayer);
		}

		return register(id, builder::build);
	}

	private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String id, Supplier<ArmorMaterial> material) {
		return AoARegistries.ARMOUR_MATERIALS.register(id, material);
	}

	static class Builder {
		private final int bootsArmour;
		private final int legsArmour;
		private final int chestplateArmour;
		private final int helmetArmour;
		private int animalArmour = 0;
		private float toughness = 0;
		private float knockbackResist = 0;
		private int enchantValue = 10;
		private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
		private Supplier<Ingredient> repairIngredient = () -> Ingredient.EMPTY;
		private List<ArmorMaterial.Layer> layers;

		Builder(int bootsArmour, int legsArmour, int chestplateArmour, int helmetArmour) {
			this.bootsArmour = bootsArmour;
			this.legsArmour = legsArmour;
			this.chestplateArmour = chestplateArmour;
			this.helmetArmour = helmetArmour;
		}

		static Builder armour(int bootsArmour, int legsArmour, int chestplateArmour, int helmetArmour) {
			return new Builder(bootsArmour, legsArmour, chestplateArmour, helmetArmour);
		}

		static Builder helmet(int helmetArmour) {
			return armour(0, 0, 0, helmetArmour);
		}

		static Builder boots(int bootsArmour) {
			return armour(bootsArmour, 0, 0, 0);
		}

		Builder animalArmour(int armour) {
			this.animalArmour = armour;

			return this;
		}

		Builder toughness(float toughness) {
			this.toughness = toughness;

			return this;
		}

		Builder knockbackResist(float knockbackResist) {
			this.knockbackResist = knockbackResist;

			return this;
		}

		Builder enchantValue(int enchantValue) {
			this.enchantValue = enchantValue;

			return this;
		}

		Builder equipSound(Holder<SoundEvent> equipSound) {
			this.equipSound = equipSound;

			return this;
		}

		Builder repairIngredient(Holder<Item> item) {
			return repairIngredient(item.value());
		}

		Builder repairIngredient(Item item) {
			return repairIngredient(() -> Ingredient.of(item));
		}

		Builder repairIngredient(Supplier<Ingredient> ingredient) {
			this.repairIngredient = ingredient;

			return this;
		}

		Builder extraLayers(ArmorMaterial.Layer... layers) {
			if (this.layers == null)
				this.layers = new ObjectArrayList<>(layers.length);

            this.layers.addAll(Arrays.asList(layers));

			return this;
		}

		ArmorMaterial build() {
			return new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, this.bootsArmour);
				map.put(ArmorItem.Type.LEGGINGS, this.legsArmour);
				map.put(ArmorItem.Type.CHESTPLATE, this.chestplateArmour);
				map.put(ArmorItem.Type.HELMET, this.helmetArmour);
				map.put(ArmorItem.Type.BODY, this.animalArmour);
			}), this.enchantValue, this.equipSound, this.repairIngredient, this.layers, this.toughness, this.knockbackResist);
		}
	}
}
