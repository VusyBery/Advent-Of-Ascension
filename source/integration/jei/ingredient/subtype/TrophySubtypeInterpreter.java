package net.tslat.aoa3.integration.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.block.functional.misc.TrophyBlock;
import net.tslat.aoa3.util.RegistryUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TrophySubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final TrophySubtypeInterpreter INSTANCE = new TrophySubtypeInterpreter();

	@Nullable
	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if (!ingredient.has(AoADataComponents.TROPHY_DATA))
			return null;

		TrophyBlock.TrophyData trophyData = ingredient.get(AoADataComponents.TROPHY_DATA);

		if (!trophyData.entityData().contains("id"))
			return null;

		return trophyData.getEntityType();
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		return Optional.ofNullable(getSubtypeData(ingredient, context))
				.map(entityType -> RegistryUtil.getId((EntityType<?>)entityType).toString())
				.orElse("");
	}
}