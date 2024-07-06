package net.tslat.aoa3.integration.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.block.functional.misc.TrophyBlock;
import net.tslat.aoa3.util.RegistryUtil;

public class TrophySubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
	public static final TrophySubtypeInterpreter INSTANCE = new TrophySubtypeInterpreter();

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		if (!ingredient.has(AoADataComponents.TROPHY_DATA))
			return "";

		TrophyBlock.TrophyData trophyData = ingredient.get(AoADataComponents.TROPHY_DATA);

		if (!trophyData.entityData().contains("id"))
			return "";

		return RegistryUtil.getId(trophyData.getEntityType()).toString();
	}
}