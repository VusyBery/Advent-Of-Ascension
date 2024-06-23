package net.tslat.aoa3.content.item.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public interface SequenceVerifiedItem {
    String getSequenceName();

    default String getSequenceStage(ItemStack stack) {
        return stack.getOrDefault(AoADataComponents.RESERVED_ITEM_STAGE, "");
    }

    default ItemStack newValidStack() {
        ItemStack stack = ((Item)this).getDefaultInstance();

        stack.set(AoADataComponents.RESERVED_ITEM_STAGE, getSequenceName());

        return stack;
    }

    default boolean verifyStack(ItemStack stack) {
        if (stack.isEmpty())
            return false;

        String stage = getSequenceStage(stack);

        return stage.equals(getSequenceName());
    }
}
