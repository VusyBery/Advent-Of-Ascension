package net.tslat.aoa3.content.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.RecipeUtil;

/**
 * Just a dummy recipe for JEI
 */
public record FrameBenchRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails, Ingredient input, ItemStack output) implements RecipeBookRecipe<SingleRecipeInput> {
	private static final RecipeType<FrameBenchRecipe> RECIPE_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return "frame_bench";
		}
	};

	public FrameBenchRecipe(ItemLike output) {
		this(new RecipeUtil.RecipeBookDetails("", CraftingBookCategory.EQUIPMENT, false), Ingredient.of(AoAItems.SCRAP_METAL), output.asItem().getDefaultInstance());
	}

	@Override
	public ItemStack getToastSymbol() {
		return AoABlocks.FRAME_BENCH.toStack();
	}

	@Override
	public boolean matches(SingleRecipeInput inv, Level world) {
		return this.input.test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider holderLookup) {
		return this.output.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height == 1;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return this.output.copy();
	}

	@Override
	public RecipeSerializer<FrameBenchRecipe> getSerializer() {
		return null;
	}

	@Override
	public RecipeType<FrameBenchRecipe> getType() {
		return RECIPE_TYPE;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(SingleRecipeInput inv) {
		return NonNullList.create();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();

		ingredients.add(this.input);

		return ingredients;
	}
}
