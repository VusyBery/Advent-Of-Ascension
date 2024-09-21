package net.tslat.aoa3.integration.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.integration.patchouli.PatchouliIntegration;

public class TornPagesSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
	public static final TornPagesSubtypeInterpreter INSTANCE = new TornPagesSubtypeInterpreter();

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		if (!IntegrationManager.isPatchouliActive())
			return "";

		return PatchouliIntegration.getBookFromStack(ingredient)
				.filter(PatchouliIntegration::isBookLoaded)
				.map(ResourceLocation::toString).orElse("");
	}
}