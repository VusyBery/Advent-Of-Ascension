package net.tslat.aoa3.content.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.tslat.aoa3.common.menu.generic.GenericRecipeInput;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.util.RecipeUtil;
import net.tslat.aoa3.util.StreamCodecUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusionRecipe implements Recipe<GenericRecipeInput> {
	private final RecipeUtil.RecipeBookDetails recipeBookDetails;

	private final NonNullList<Ingredient> ingredients;
	private final Ingredient input;
	private final ItemStack output;

	private final boolean isSimple;

	public InfusionRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification, NonNullList<Ingredient> ingredients, Ingredient input, ItemStack output) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification), ingredients, input, output);
	}

	public InfusionRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails, NonNullList<Ingredient> ingredients, Ingredient input, ItemStack output) {
		this.recipeBookDetails = recipeBookDetails;
		this.ingredients = ingredients;
		this.input = input;
		this.output = output;
		boolean isSimple = true;

		for (Ingredient ingredient : ingredients) {
			if (!ingredient.isSimple()) {
				isSimple = false;

				break;
			}
		}

		this.isSimple = isSimple;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AoABlocks.INFUSION_TABLE.get());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= getIngredients().size() + 1;
	}

	@Override
	public String getGroup() {
		return this.recipeBookDetails.group();
	}

	@Override
	public boolean showNotification() {
		return this.recipeBookDetails.showUnlockNotification();
	}

	@Override
	public RecipeSerializer<InfusionRecipe> getSerializer() {
		return AoARecipes.INFUSION.serializer().get();
	}

	@Override
	public RecipeType<InfusionRecipe> getType() {
		return AoARecipes.INFUSION.type().get();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	public Ingredient getInput() {
		return this.input;
	}

	@Override
	public boolean matches(GenericRecipeInput inventory, Level level) {
		final ItemStack inputStack = inventory.getItem(0);

		if (inputStack.isEmpty() || !this.input.test(inputStack))
			return false;

		final List<Ingredient> ingredients = getIngredients();

		return this.isSimple ? checkSimpleIngredients(inventory, ingredients.size(), inputStack) : checkNonSimpleIngredients(inventory, ingredients, inputStack);
	}

	private boolean checkSimpleIngredients(GenericRecipeInput inventory, int ingredientsCount, ItemStack inputStack) {
		StackedContents itemHelper = new StackedContents();

		for (ItemStack ingredient : inventory.inputs()) {
			if (ingredient.isEmpty() || ingredient == inputStack)
				continue;

			if (ingredientsCount-- < 0)
				return false;

			itemHelper.accountStack(ingredient, 1);
		}

		return ingredientsCount == 0 && itemHelper.canCraft(this, null);
	}

	private boolean checkNonSimpleIngredients(GenericRecipeInput inventory, List<Ingredient> ingredients, ItemStack inputStack) {
		int ingredientsCount = ingredients.size();
		List<ItemStack> foundIngredients = new ObjectArrayList<>(ingredientsCount);

		for (ItemStack ingredient : inventory.inputs()) {
			if (ingredient.isEmpty() || ingredient == inputStack)
				continue;

			if (ingredientsCount-- < 0)
				return false;

			foundIngredients.add(ingredient);
		}

		return ingredientsCount == 0 && RecipeMatcher.findMatches(foundIngredients, ingredients) != null;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(GenericRecipeInput inv) {
		final NonNullList<ItemStack> returns = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < returns.size(); i++) {
			ItemStack stack = inv.getItem(i);

			if (stack.hasCraftingRemainingItem())
				returns.set(i, CommonHooks.getCraftingRemainingItem(stack));
		}

		return returns;
	}

	@Override
	public ItemStack assemble(GenericRecipeInput inv, HolderLookup.Provider holderLookup) {
		return getResultItem(holderLookup);
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return this.output.copy();
	}

	public static class Factory implements RecipeSerializer<InfusionRecipe> {
		public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, instance -> instance.recipeBookDetails).and(builder.group(
								Ingredient.CODEC_NONEMPTY
										.listOf()
										.fieldOf("ingredients")
										.flatXmap(
												ingredients -> {
													final Ingredient[] ingredientArray = ingredients.toArray(Ingredient[]::new);

													if (ingredientArray.length == 0)
														return DataResult.error(() -> "No ingredients for Infusion recipe");

													return ingredientArray.length > ShapedRecipePattern.getMaxWidth() * ShapedRecipePattern.getMaxHeight()
															? DataResult.error(() -> "Too many ingredients for Infusion recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxWidth() * ShapedRecipePattern.getMaxHeight()))
															: DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArray));
												},
												DataResult::success)
										.forGetter(instance -> instance.ingredients),
								Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(instance -> instance.input),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(instance -> instance.output)))
						.apply(builder, InfusionRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, recipe -> recipe.recipeBookDetails,
				StreamCodecUtil.nonNullList(Ingredient.CONTENTS_STREAM_CODEC, Ingredient.EMPTY), recipe -> recipe.ingredients,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
				ItemStack.STREAM_CODEC, recipe -> recipe.output,
				InfusionRecipe::new);

		@Override
		public MapCodec<InfusionRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}