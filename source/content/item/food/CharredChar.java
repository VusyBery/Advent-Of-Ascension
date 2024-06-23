package net.tslat.aoa3.content.item.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.tslat.aoa3.common.registration.item.AoAFood;
import org.jetbrains.annotations.Nullable;

public class CharredChar extends Item {
	public CharredChar() {
		super(new Item.Properties().food(AoAFood.CHARRED_CHAR));
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return 1200;
	}
}
