package net.tslat.aoa3.mixin.common.function;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.tslat.aoa3.event.custom.AoAEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingMenu.class)
public class WorkbenchContainerMixin {
	@WrapOperation(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private static void aoa3$fireCraftingEvent(ResultContainer container, int slotIndex, ItemStack stack, Operation<Void> original, @Local(argsOnly = true, index = 2) Player player, @Local(argsOnly = true, index = 3) CraftingContainer craftingInventory) {
		if (AoAEvents.firePlayerCraftingEvent(player, stack, craftingInventory, container))
			stack = ItemStack.EMPTY;

		original.call(container, slotIndex, stack);
	}
}
