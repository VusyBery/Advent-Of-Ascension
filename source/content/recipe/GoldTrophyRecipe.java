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
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.block.functional.misc.TrophyBlock;
import net.tslat.aoa3.util.RecipeUtil;

public class GoldTrophyRecipe extends CustomRecipe implements RecipeBookRecipe<CraftingInput> {
	private final RecipeUtil.RecipeBookDetails recipeBookDetails;
	private final NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.of(AoABlocks.TROPHY.get()));

	public GoldTrophyRecipe(String group, CraftingBookCategory category, boolean showObtainNotification) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification));
	}

	public GoldTrophyRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails) {
		super(recipeBookDetails.category());

		this.recipeBookDetails = recipeBookDetails;
	}

	@Override
	public RecipeUtil.RecipeBookDetails recipeBookDetails() {
		return this.recipeBookDetails;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.GOLD_TROPHY.serializer().get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public boolean matches(CraftingInput input, Level worldIn) {
		if (input.ingredientCount() != 9)
			return false;

		String entityType = null;

		for (ItemStack stack : input.items()) {
			if (stack.getItem() != AoABlocks.TROPHY.get().asItem())
				return false;

			TrophyBlock.TrophyData trophyData = stack.get(AoADataComponents.TROPHY_DATA);
			String entityId;

			if (trophyData == null || !trophyData.isOriginalTrophy() || (entityId = trophyData.entityData().getString("id")).isEmpty())
				return false;

			if (entityType == null) {
				if (trophyData.getEntityType() == null)
					return false;

				entityType = entityId;

				continue;
			}

			if (!entityType.equals(entityId))
				return false;
		}

		return true;
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider holderLookup) {
		for (int i = 0; i < inv.size(); i++) {
			if (inv.getItem(i).getItem() == AoABlocks.TROPHY.get().asItem())
				return TrophyBlock.cloneTrophy(inv.getItem(i), AoABlocks.GOLD_TROPHY.get());
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider holderLookup) {
		return new ItemStack(AoABlocks.GOLD_TROPHY.get());
	}

	public static class Factory implements RecipeSerializer<GoldTrophyRecipe> {
		public static final MapCodec<GoldTrophyRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, GoldTrophyRecipe::recipeBookDetails)
						.apply(builder, GoldTrophyRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, GoldTrophyRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, GoldTrophyRecipe::recipeBookDetails,
				GoldTrophyRecipe::new);

		@Override
		public MapCodec<GoldTrophyRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, GoldTrophyRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
