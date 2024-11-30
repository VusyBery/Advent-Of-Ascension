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
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.menu.generic.ExtensibleRecipeMenu;
import net.tslat.aoa3.common.menu.provider.GenericMenuProvider;
import net.tslat.aoa3.common.menu.slot.OutputSlot;
import net.tslat.aoa3.common.menu.slot.PredicatedSlot;
import net.tslat.aoa3.common.registration.AoAMenus;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.item.AoAItems;

public class TinkerersTableMenu extends ExtensibleRecipeMenu<TransientCraftingContainer, SingleRecipeInput> {
	public TinkerersTableMenu(int screenId, Inventory playerInventory, ContainerLevelAccess accessValidator) {
		super(AoAMenus.TINKERERS_TABLE.get(), screenId, playerInventory, accessValidator);

		createPlayerInventory(playerInventory, 8, 84);
	}

	@Override
	protected SingleRecipeInput createRecipeInput() {
		return new SingleRecipeInput(getInventory().getItems().get(0));
	}

	@Override
    public int inputSlotCount() {
		return 1;
	}

	@Override
	protected TransientCraftingContainer createInventory() {
		return new TransientCraftingContainer(this, 1, 1, NonNullList.withSize(1, ItemStack.EMPTY));
	}

	@Override
	protected Block getContainerBlock() {
		return AoABlocks.FRAME_BENCH.get();
	}

	@Override
	protected Slot createInputSlot(int slotIndex, TransientCraftingContainer inventory) {
		return new PredicatedSlot(inventory, slotIndex, 11, 34, stack -> stack.getItem() == AoAItems.SCRAP_METAL.get());
	}

	@Override
	protected Slot createOutputSlot(int slotIndex, Player player) {
		return new OutputSlot(new ResultContainer(), player, slotIndex, 149, 34) {
			@Override
			public void onItemRemoved(Player player, ItemStack stack) {
				consumeInputItem(0, 1);
			}
		};
	}

	@Override
	protected void handleContainerUpdate() {

	}

	@Override
	public boolean clickMenuButton(Player player, int data) {

		handleContainerUpdate();

		return true;
	}

	public static void openContainer(ServerPlayer player, BlockPos pos) {
		player.openMenu(new GenericMenuProvider("tinkerers_table", pos, TinkerersTableMenu::new), pos);
	}
}