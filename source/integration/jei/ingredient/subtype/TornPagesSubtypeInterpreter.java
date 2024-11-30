package net.tslat.aoa3.integration.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.integration.patchouli.PatchouliIntegration;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TornPagesSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final TornPagesSubtypeInterpreter INSTANCE = new TornPagesSubtypeInterpreter();

	@Nullable
	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if (!IntegrationManager.isPatchouliActive())
			return null;

		return PatchouliIntegration.getBookFromStack(ingredient)
				.filter(PatchouliIntegration::isBookLoaded)
				.orElse(null);
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		return Optional.ofNullable(getSubtypeData(ingredient, context)).map(Object::toString).orElse(null);
	}
}