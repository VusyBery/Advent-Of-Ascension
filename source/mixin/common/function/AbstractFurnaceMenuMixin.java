package net.tslat.aoa3.mixin.common.function;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.event.custom.AoAEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFurnaceMenu.class)
public class AbstractFurnaceMenuMixin {
	@ModifyExpressionValue(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
	public ItemStack aoa3$fireSmeltedEvent(ItemStack stackCopy, @Local(argsOnly = true, ordinal = 0) Player player, @Local(argsOnly = true, ordinal = 0) int slotIndex, @Share("aoa3$originalStack") LocalRef<ItemStack> originalStack, @Share("aoa3$modifiedExtraStackCount") LocalIntRef modAddedStackCount) {
		if (slotIndex != 2)
			return stackCopy;

		Slot slot = ((AbstractContainerMenu)(Object)this).slots.get(slotIndex);
		ItemStack stack = slot.getItem();

		originalStack.set(stackCopy);
		AoAEvents.firePlayerRetrieveSmeltedEvent(player, stack, slot.container);
		modAddedStackCount.set(stack.getCount() - stackCopy.getCount());

		return stack.copy();
	}

	@ModifyReturnValue(method = "quickMoveStack", at = @At(value = "RETURN", ordinal = 0))
	private ItemStack aoa3$resetCapturedStack(ItemStack emptyStack, @Local(argsOnly = true, ordinal = 0) int slotIndex, @Share("aoa3$originalStack") LocalRef<ItemStack> originalStack, @Share("aoa3$modifiedExtraStackCount") LocalIntRef modAddedStackCount) {
		Slot slot = ((AbstractContainerMenu)(Object)this).slots.get(slotIndex);
		ItemStack stack = originalStack.get();

		stack.setCount(Math.max(0, slot.getItem().getCount() - modAddedStackCount.get()));
		slot.container.setItem(slot.getSlotIndex(), stack);

		return emptyStack;
	}
}
