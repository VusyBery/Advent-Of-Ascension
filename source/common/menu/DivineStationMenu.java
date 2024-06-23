package net.tslat.aoa3.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.menu.generic.ExtensibleRecipeMenu;
import net.tslat.aoa3.common.menu.generic.GenericRecipeInput;
import net.tslat.aoa3.common.menu.provider.GenericMenuProvider;
import net.tslat.aoa3.common.menu.slot.CraftableResultSlot;
import net.tslat.aoa3.common.menu.slot.OutputSlot;
import net.tslat.aoa3.common.registration.AoAMenus;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.block.AoABlocks;

public class DivineStationMenu extends ExtensibleRecipeMenu<TransientCraftingContainer, GenericRecipeInput> {
	public DivineStationMenu(int screenId, Inventory playerInventory, ContainerLevelAccess accessValidator) {
		super(AoAMenus.DIVINE_STATION.get(), screenId, playerInventory, accessValidator);

		createPlayerInventory(playerInventory, 8, 60);
	}

	@Override
	protected GenericRecipeInput createRecipeInput() {
		return new GenericRecipeInput(getInventory().getItems());
	}

	@Override
    public int inputSlotCount() {
		return 2;
	}

	@Override
	protected TransientCraftingContainer createInventory() {
		return new TransientCraftingContainer(this, 2, 1, NonNullList.withSize(2, ItemStack.EMPTY));
	}

	@Override
	protected Block getContainerBlock() {
		return AoABlocks.DIVINE_STATION.get();
	}

	@Override
	protected Slot createInputSlot(int slotIndex, TransientCraftingContainer inventory) {
		return new Slot(inventory, slotIndex, slotIndex == 0 ? 27 : 76, 23);
	}

	@Override
	protected Slot createOutputSlot(int slotIndex, Player player) {
		return new CraftableResultSlot<>(player, this.inventory, new ResultContainer(), AoARecipes.UPGRADE_KIT.type().get(), this::createRecipeInput, slotIndex, 134, 23);
	}

	@Override
	protected void handleContainerUpdate() {
		final OutputSlot outputSlot = (OutputSlot)getOutputSlot();

		updateRecipeOutput(AoARecipes.UPGRADE_KIT.type().get(), outputSlot.getPlayer(), (ResultContainer)outputSlot.container, recipe -> true);
	}

	public static void openContainer(ServerPlayer player, BlockPos pos) {
		player.openMenu(new GenericMenuProvider("divine_station", pos, DivineStationMenu::new), pos);
	}
}
