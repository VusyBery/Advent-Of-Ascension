package net.tslat.aoa3.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tslat.aoa3.common.menu.generic.ExtensibleContainerMenu;
import net.tslat.aoa3.common.registration.AoAMenus;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.block.blockentity.InfusedPressBlockEntity;

public class InfusedPressMenu extends ExtensibleContainerMenu<SimpleContainer> {
	public InfusedPressMenu(int screenId, Inventory playerInventory, ContainerLevelAccess accessValidator) {
		super(AoAMenus.INFUSED_PRESS.get(), screenId, playerInventory, accessValidator);

		createPlayerInventory(playerInventory, 8, 84);
	}

	@Override
    public int inputSlotCount() {
		return 9;
	}

	@Override
	protected SimpleContainer createInventory() {
		return new SimpleContainer(inputSlotCount() + 1);
	}

	@Override
	protected Block getContainerBlock() {
		return AoABlocks.INFUSED_PRESS.get();
	}

	@Override
	protected Slot createInputSlot(int slotIndex, SimpleContainer inventory) {
		return new Slot(inventory, slotIndex, 30 + gridXFromIndex(slotIndex) * 18, 17 + gridYFromIndex(slotIndex) * 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(AoAItems.COMPRESSED_ITEM) || stack.isStackable();
			}
		};
	}

	@Override
	protected Slot createOutputSlot(int slotIndex, Player player) {
		return new Slot(getInventory(), 9, 124, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(AoAItems.COMPRESSED_ITEM) && stack.has(AoADataComponents.COMPRESSED_ITEM_DATA) && stack.get(AoADataComponents.COMPRESSED_ITEM_DATA).compressions() > 0;
			}
		};
	}

	@Override
	protected void handleContainerUpdate() {}

	public static void openContainer(ServerPlayer player, BlockPos pos) {
		final BlockEntity blockEntity = player.level().getBlockEntity(pos);

		if (!(blockEntity instanceof InfusedPressBlockEntity press))
			return;

		player.openMenu(press, pos);
	}
}