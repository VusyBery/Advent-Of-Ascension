package net.tslat.aoa3.content.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.tslat.aoa3.util.RecipeUtil;

public interface RecipeBookRecipe<T extends RecipeInput> extends Recipe<T> {
    RecipeUtil.RecipeBookDetails recipeBookDetails();

    @Override
    default String getGroup() {
        return recipeBookDetails().group();
    }

    @Override
    default boolean showNotification() {
        return recipeBookDetails().showUnlockNotification();
    }
}
