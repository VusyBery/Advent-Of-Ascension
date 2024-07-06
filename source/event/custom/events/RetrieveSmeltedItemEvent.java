package net.tslat.aoa3.event.custom.events;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class RetrieveSmeltedItemEvent extends PlayerEvent {
	@NotNull
	private final ItemStack outputStack;
	@NotNull
	private final ItemStack originalStack;
	private final Container outputSlotInventory;

	public RetrieveSmeltedItemEvent(Player player, @NotNull ItemStack smelting, Container outputSlotInventory) {
		super(player);

		this.originalStack = smelting.copy();
		this.outputStack = smelting;
		this.outputSlotInventory = outputSlotInventory;
	}

	@NotNull
	public ItemStack getOriginalStack() {
		return this.originalStack;
	}

	@NotNull
	public ItemStack getOutputStack() {
		return this.outputStack;
	}

	public Container getSlotInventory() {
		return this.outputSlotInventory;
	}
}
