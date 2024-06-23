package net.tslat.aoa3.content.loottable.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.tslat.aoa3.common.registration.loot.AoALootFunctions;

import java.util.Arrays;
import java.util.List;

public class EnchantSpecific extends LootItemConditionalFunction {
	public static final MapCodec<EnchantSpecific> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(
			ItemEnchantments.CODEC.fieldOf("enchantments").forGetter(EnchantSpecific::getEnchantments))
			.apply(builder, EnchantSpecific::new));

	private final ItemEnchantments enchants;

	protected EnchantSpecific(List<LootItemCondition> lootConditions, ItemEnchantments enchantments) {
		super(lootConditions);

		this.enchants = enchantments;
	}

	@Override
	public LootItemFunctionType<EnchantSpecific> getType() {
		return AoALootFunctions.ENCHANT_SPECIFIC.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		EnchantmentHelper.setEnchantments(stack, this.enchants);

		return stack;
	}

	public ItemEnchantments getEnchantments() {
		return this.enchants;
	}

	public static Builder<?> builder(boolean hideInTooltip, ObjectIntPair<Holder<Enchantment>>... enchantments) {
		ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY.withTooltip(!hideInTooltip));

		Arrays.stream(enchantments).forEachOrdered(pair -> builder.set(pair.left(), pair.rightInt()));

		return simpleBuilder((conditions) -> new EnchantSpecific(conditions, builder.toImmutable()));
	}
}
