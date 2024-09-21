package net.tslat.aoa3.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.menu.generic.GenericRecipeInput;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.item.datacomponent.CompressedItemData;
import net.tslat.aoa3.content.item.misc.CompressedItem;
import net.tslat.aoa3.util.RecipeUtil;
import org.jetbrains.annotations.Nullable;

public record InfusedPressCompressionRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails) implements RecipeBookRecipe<GenericRecipeInput> {
	public InfusedPressCompressionRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification));
	}

	@Override
	public RecipeType<?> getType() {
		return AoARecipes.INFUSED_PRESS_COMPRESSION.type().get();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.INFUSED_PRESS_COMPRESSION.serializer().get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public boolean matches(GenericRecipeInput inv, Level worldIn) {
		ItemStack firstStack = null;
		int count = 0;
		int compressedValue = 0;

		if (!inv.getItem(9).isEmpty())
			return false;

		for (int i = 0; i < 9; i++) {
			ItemStack stack = inv.getItem(i);

			if (stack.isEmpty())
				continue;

			if (stack.is(AoAItems.COMPRESSED_ITEM)) {
				if (!stack.has(AoADataComponents.COMPRESSED_ITEM_DATA))
					return false;

				CompressedItemData data = stack.get(AoADataComponents.COMPRESSED_ITEM_DATA);
				int compressions = data.compressions();

				if (CompressedItem.getCompressedCount(data.compressedStack().getMaxStackSize(), compressions + 1) < 0)
					return false;

				if (compressedValue == 0) {
					compressedValue = compressions;
				}
				else if (compressedValue != compressions) {
					return false;
				}
			}
			else if (!stack.isStackable() || stack.getCount() < stack.getMaxStackSize()) {
				return false;
			}

			if (firstStack == null) {
				firstStack = stack;
				count++;

				continue;
			}

			if (!ItemStack.isSameItemSameComponents(firstStack, stack))
				return false;

			count++;
		}

		return count == 9;
	}

	@Override
	public ItemStack assemble(GenericRecipeInput inv, HolderLookup.Provider holderLookup) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);

			if (!stack.isEmpty()) {
				ItemStack newStack;

				if (stack.is(AoAItems.COMPRESSED_ITEM)) {
					newStack = stack.copy();
					newStack.set(AoADataComponents.COMPRESSED_ITEM_DATA, stack.get(AoADataComponents.COMPRESSED_ITEM_DATA).compress());
				}
				else {
					newStack = AoAItems.COMPRESSED_ITEM.toStack();
					newStack.set(AoADataComponents.COMPRESSED_ITEM_DATA, new CompressedItemData(stack.copy(), 1));
				}

				return newStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return AoAItems.COMPRESSED_ITEM.toStack();
	}

	public static class Factory implements RecipeSerializer<InfusedPressCompressionRecipe> {
		public static final MapCodec<InfusedPressCompressionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, InfusedPressCompressionRecipe::recipeBookDetails)
						.apply(builder, InfusedPressCompressionRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, InfusedPressCompressionRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, InfusedPressCompressionRecipe::recipeBookDetails,
				InfusedPressCompressionRecipe::new);

		@Override
		public MapCodec<InfusedPressCompressionRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, InfusedPressCompressionRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
