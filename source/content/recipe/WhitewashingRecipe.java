package net.tslat.aoa3.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.menu.generic.GenericRecipeInput;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.util.RecipeUtil;
import org.jetbrains.annotations.Nullable;

public record WhitewashingRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails, Ingredient input, Ingredient washingMaterial, ItemStack output) implements RecipeBookRecipe<GenericRecipeInput> {
	public WhitewashingRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification, Ingredient input, Ingredient washingMaterial, ItemStack output) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification), input, washingMaterial, output);
	}

	@Override
	public RecipeSerializer<WhitewashingRecipe> getSerializer() {
		return AoARecipes.WHITEWASHING.serializer().get();
	}

	@Override
	public RecipeType<WhitewashingRecipe> getType() {
		return AoARecipes.WHITEWASHING.type().get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AoABlocks.WHITEWASHING_TABLE.get());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(null, this.input, this.washingMaterial);
	}

	@Override
	public boolean matches(GenericRecipeInput inv, Level world) {
		return this.input.test(inv.getItem(0)) && this.washingMaterial.test(inv.getItem(1));
	}

	@Override
	public ItemStack assemble(GenericRecipeInput recipeInput, HolderLookup.Provider holderLookup) {
		return getResultItem(holderLookup);
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return this.output.copy();
	}

	public static class Factory implements RecipeSerializer<WhitewashingRecipe> {
		public static final MapCodec<WhitewashingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, instance -> instance.recipeBookDetails).and(builder.group(
								Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(instance -> instance.input),
								Ingredient.CODEC_NONEMPTY.fieldOf("washing_material").forGetter(instance -> instance.washingMaterial),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(instance -> instance.output)))
						.apply(builder, WhitewashingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, WhitewashingRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, recipe -> recipe.recipeBookDetails,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.washingMaterial,
				ItemStack.STREAM_CODEC, recipe -> recipe.output,
				WhitewashingRecipe::new);

		@Override
		public MapCodec<WhitewashingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, WhitewashingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
