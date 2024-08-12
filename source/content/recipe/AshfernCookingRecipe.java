package net.tslat.aoa3.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.RecipeUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.Optional;

public class AshfernCookingRecipe extends CustomRecipe implements RecipeBookRecipe<CraftingInput> {
	private final RecipeUtil.RecipeBookDetails recipeBookDetails;

	public AshfernCookingRecipe(String group, CraftingBookCategory category, boolean showObtainNotification) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification));
	}

	public AshfernCookingRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails) {
		super(recipeBookDetails.category());

		this.recipeBookDetails = recipeBookDetails;
	}

	public RecipeUtil.RecipeBookDetails recipeBookDetails() {
		return this.recipeBookDetails;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.ASHFERN_COOKING.serializer().get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		boolean hasFern = false;
		boolean hasFood = false;

		for (ItemStack stack : container.items()) {
			if (stack.isEmpty())
				continue;

			if (stack.is(AoAItems.ASHFERN.get())) {
				if (hasFern)
					return false;

				hasFern = true;

				continue;
			}

			if (hasFood || stack.getFoodProperties(null) == null)
				return false;

			Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level);

			if (smeltingRecipe.isPresent() && !smeltingRecipe.get().value().getResultItem(level.registryAccess()).isEmpty()) {
				hasFood = true;
			}
			else {
				return false;
			}
		}

		return hasFern && hasFood;
	}

	@Override
	public ItemStack assemble(CraftingInput container, HolderLookup.Provider holderLookup) {
		final Level level = WorldUtil.getServer().getLevel(AoADimensions.OVERWORLD);

		if (level == null)
			return ItemStack.EMPTY;

		boolean foundFern = false;
		ItemStack output = ItemStack.EMPTY;

		for (int i = 0; i < container.size(); i++) {
			final ItemStack stack = container.getItem(i);

			if (stack.isEmpty())
				continue;

			if (stack.is(AoAItems.ASHFERN.get())) {
				foundFern = true;

				if (!output.isEmpty())
					break;

				continue;
			}

			Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level);

			if (smeltingRecipe.isPresent())
				output = smeltingRecipe.get().value().getResultItem(holderLookup);

			if (foundFern)
				break;
		}

		if (!foundFern)
			return ItemStack.EMPTY;

		output = output.copy();

		return output;
	}

	public static class Factory implements RecipeSerializer<AshfernCookingRecipe> {
		public static final MapCodec<AshfernCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, AshfernCookingRecipe::recipeBookDetails)
						.apply(builder, AshfernCookingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, AshfernCookingRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, AshfernCookingRecipe::recipeBookDetails,
				AshfernCookingRecipe::new);

		@Override
		public MapCodec<AshfernCookingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, AshfernCookingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
