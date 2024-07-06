package net.tslat.aoa3.mixin.common.function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.event.custom.AoAEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFurnaceMenu.class)
public class AbstractFurnaceMenuMixin {
	@WrapOperation(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractFurnaceMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z", ordinal = 0))
	public boolean aoa3$resetStack(AbstractFurnaceMenu menu, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, Operation<Boolean> callback, @Local(argsOnly = true, ordinal = 0) Player player, @Local(argsOnly = true, ordinal = 0) int slotIndex) {
		ItemStack clone = stack.copy();

		AoAEvents.firePlayerRetrieveSmeltedEvent(player, clone, menu.getSlot(slotIndex).container);

		boolean fullyMoved = callback.call(menu, clone, startIndex, endIndex, reverseDirection);

		stack.setCount(Math.min(stack.getCount(), clone.getCount()));

		return fullyMoved;
	}
}
