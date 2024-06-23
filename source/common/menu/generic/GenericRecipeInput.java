package net.tslat.aoa3.common.menu.generic;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record GenericRecipeInput(List<ItemStack> inputs) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return inputs.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof GenericRecipeInput genericRecipeInput))
            return false;

        return ItemStack.listMatches(this.inputs, genericRecipeInput.inputs);
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.inputs);
    }

    @Override
    public int size() {
        return inputs.size();
    }
}
