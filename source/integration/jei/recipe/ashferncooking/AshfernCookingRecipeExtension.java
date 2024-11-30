package net.tslat.aoa3.integration.jei.recipe.ashferncooking;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.recipe.AshfernCookingRecipe;

import java.util.List;

public class AshfernCookingRecipeExtension implements ICraftingCategoryExtension<AshfernCookingRecipe> {
	@Override
	public void setRecipe(RecipeHolder<AshfernCookingRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		final Level level = Minecraft.getInstance().level;

		if (level == null)
			return;

		final List<ItemStack> inputs = new ObjectArrayList<>();
		final List<ItemStack> outputs = new ObjectArrayList<>();

		BuiltInRegistries.ITEM.stream().forEach(item -> {
			if (item.components().has(DataComponents.FOOD)) {
				final ItemStack stack = item.getDefaultInstance();

				level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level).map(RecipeHolder::value)
						.map(recipe -> recipe.getResultItem(level.registryAccess()))
						.ifPresent(smelted -> {
							inputs.add(stack);
							outputs.add(smelted);
						});
			}
		});

		final List<IRecipeSlotBuilder> ingredientSlots = craftingGridHelper.createAndSetInputs(builder, List.of(List.of(AoAItems.ASHFERN.toStack()), inputs), 0, 0);
		final IRecipeSlotBuilder resultSlot = craftingGridHelper.createAndSetOutputs(builder, outputs);

		builder.setShapeless();
		builder.createFocusLink(ingredientSlots.get(1), resultSlot);
	}

	@Override
	public int getWidth(RecipeHolder<AshfernCookingRecipe> recipeHolder) {
		return 0;
	}

	@Override
	public int getHeight(RecipeHolder<AshfernCookingRecipe> recipeHolder) {
		return 0;
	}
}
