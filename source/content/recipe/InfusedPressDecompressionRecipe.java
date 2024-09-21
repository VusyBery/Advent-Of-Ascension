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
import net.tslat.aoa3.util.RecipeUtil;
import org.jetbrains.annotations.Nullable;

public record InfusedPressDecompressionRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails) implements RecipeBookRecipe<GenericRecipeInput> {
	public InfusedPressDecompressionRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification));
	}

	@Override
	public RecipeType<?> getType() {
		return AoARecipes.INFUSED_PRESS_DECOMPRESSION.type().get();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.INFUSED_PRESS_DECOMPRESSION.serializer().get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public boolean matches(GenericRecipeInput inv, Level level) {
		if (inv.size() < 10)
			return false;

		ItemStack stack = inv.getItem(9);

		if (!stack.is(AoAItems.COMPRESSED_ITEM) || !stack.has(AoADataComponents.COMPRESSED_ITEM_DATA))
			return false;

		for (int i = 0; i < 9; i++) {
			if (!inv.getItem(i).isEmpty())
				return false;
		}

		CompressedItemData data = stack.get(AoADataComponents.COMPRESSED_ITEM_DATA);

        return !data.compressedStack().isEmpty() && data.compressions() >= 1;
    }

	@Override
	public ItemStack assemble(GenericRecipeInput inv, HolderLookup.Provider holderLookup) {
		if (inv.size() >= 10) {
			for (int i = 0; i < 9; i++) {
				if (!inv.getItem(i).isEmpty())
					return ItemStack.EMPTY;
			}

			ItemStack stack = inv.getItem(9);
			CompressedItemData data = stack.get(AoADataComponents.COMPRESSED_ITEM_DATA);
			ItemStack newStack;

			if (data.compressions() == 1) {
				newStack = data.compressedStack().copy();
			}
			else {
				newStack = stack.copy();
				newStack.set(AoADataComponents.COMPRESSED_ITEM_DATA, data.decompress());
			}

			return newStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return AoAItems.COMPRESSED_ITEM.toStack();
	}

	public static class Factory implements RecipeSerializer<InfusedPressDecompressionRecipe> {
		public static final MapCodec<InfusedPressDecompressionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, InfusedPressDecompressionRecipe::recipeBookDetails)
						.apply(builder, InfusedPressDecompressionRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, InfusedPressDecompressionRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, InfusedPressDecompressionRecipe::recipeBookDetails,
				InfusedPressDecompressionRecipe::new);

		@Override
		public MapCodec<InfusedPressDecompressionRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, InfusedPressDecompressionRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
