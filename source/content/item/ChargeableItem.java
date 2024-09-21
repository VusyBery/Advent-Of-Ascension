package net.tslat.aoa3.content.item;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public interface ChargeableItem {
    default float minRequiredCharge() {
        return 0;
    }

    default float maxCharge() {
        return Float.MAX_VALUE;
    }

    default boolean hasEnoughCharge(ItemStack stack) {
        return getCharge(stack) >= minRequiredCharge();
    }

    default float getCharge(ItemStack stack) {
        return stack.getOrDefault(AoADataComponents.CHARGE, 0f);
    }

    default void setCharge(ItemStack stack, float charge) {
        stack.set(AoADataComponents.CHARGE, Mth.clamp(charge, 0, maxCharge()));
    }

    default void addCharge(ItemStack stack, float charge) {
        stack.set(AoADataComponents.CHARGE, Math.min(getCharge(stack) + charge, maxCharge()));
    }

    default float subtractCharge(ItemStack stack, float subtraction, boolean force) {
        float currentCharge = getCharge(stack);

        if (currentCharge < subtraction && !force)
            return 0;

        float newValue = Math.max(0, currentCharge - subtraction);

        setCharge(stack, newValue);

        return currentCharge - newValue;
    }
}
