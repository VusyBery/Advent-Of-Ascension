package net.tslat.aoa3.integration.jei.recipe.staffcharging;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.content.recipe.StaffChargingRecipe;

public class StaffChargingRecipeCategory extends CraftingRecipeCategory {
	public static final RecipeType<StaffChargingRecipe> RECIPE_TYPE = RecipeType.create(AdventOfAscension.MOD_ID, "staff_charging", StaffChargingRecipe.class);

	public StaffChargingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
	}
}
