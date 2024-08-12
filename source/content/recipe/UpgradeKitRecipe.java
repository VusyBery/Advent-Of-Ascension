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

public record UpgradeKitRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails, Ingredient input, Ingredient upgradeKit, ItemStack output) implements RecipeBookRecipe<GenericRecipeInput> {
	public UpgradeKitRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification, Ingredient input, Ingredient upgradeKit, ItemStack output) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification), input, upgradeKit, output);
	}

	@Override
	public RecipeSerializer<UpgradeKitRecipe> getSerializer() {
		return AoARecipes.UPGRADE_KIT.serializer().get();
	}

	@Override
	public RecipeType<UpgradeKitRecipe> getType() {
		return AoARecipes.UPGRADE_KIT.type().get();
	}

	@Override
	public ItemStack getToastSymbol() {
		return AoABlocks.DIVINE_STATION.toStack();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(null, this.input, this.upgradeKit);
	}

	@Override
	public boolean matches(GenericRecipeInput inv, Level world) {
		return this.input.test(inv.getItem(0)) && this.upgradeKit.test(inv.getItem(1));
	}

	@Override
	public ItemStack assemble(GenericRecipeInput inv, HolderLookup.Provider holderLookup) {
		return getResultItem(holderLookup);
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return this.output.copy();
	}

	public static class Factory implements RecipeSerializer<UpgradeKitRecipe> {
		public static final MapCodec<UpgradeKitRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, UpgradeKitRecipe::recipeBookDetails).and(builder.group(
								Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(UpgradeKitRecipe::input),
								Ingredient.CODEC_NONEMPTY.fieldOf("upgrade_kit").forGetter(UpgradeKitRecipe::upgradeKit),
								ItemStack.STRICT_CODEC.fieldOf("result").forGetter(UpgradeKitRecipe::output)))
						.apply(builder, UpgradeKitRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeKitRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, UpgradeKitRecipe::recipeBookDetails,
				Ingredient.CONTENTS_STREAM_CODEC, UpgradeKitRecipe::input,
				Ingredient.CONTENTS_STREAM_CODEC, UpgradeKitRecipe::upgradeKit,
				ItemStack.STREAM_CODEC, UpgradeKitRecipe::output,
				UpgradeKitRecipe::new);

		@Override
		public MapCodec<UpgradeKitRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, UpgradeKitRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
