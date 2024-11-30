package net.tslat.aoa3.content.loottable.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.tslat.aoa3.common.registration.loot.AoALootFunctions;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.integration.patchouli.PatchouliIntegration;

import java.util.List;

public class SetPatchouliBook extends LootItemConditionalFunction {
	public static final MapCodec<SetPatchouliBook> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(
			ResourceLocation.CODEC.fieldOf("book_id").forGetter(SetPatchouliBook::getBookId))
			.apply(builder, SetPatchouliBook::new));

	private final ResourceLocation bookId;

	protected SetPatchouliBook(List<LootItemCondition> lootConditions, ResourceLocation bookId) {
		super(lootConditions);

		this.bookId = bookId;
	}

	public ResourceLocation getBookId() {
		return this.bookId;
	}

	@Override
	public LootItemFunctionType<SetPatchouliBook> getType() {
		return AoALootFunctions.SET_PATCHOULI_BOOK.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (IntegrationManager.isPatchouliActive())
			PatchouliIntegration.setPatchouliBook(stack, this.bookId);

		return stack;
	}

	public static LootItemFunction.Builder forBook(ResourceLocation bookId) {
		return () -> new SetPatchouliBook(List.of(), bookId);
	}
}
