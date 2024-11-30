package net.tslat.aoa3.event.custom.events;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the {@link net.minecraft.world.inventory.GrindstoneMenu Grindstone menu} tries to compute a result item from input items.
 * <p>
 * The original result stack may be an empty stack, as this event fires even if the result is empty.<br>
 * Use this event to modify the result of the Grindstone.
 * <p>
 * This event is {@link ICancellableEvent cancellable}
 */
public class GrindstoneResultEvent extends PlayerEvent implements ICancellableEvent {
	@NotNull
	private final ItemStack outputStack;
	private final Container inputs;

	private ItemStack newOutputStack;

	public GrindstoneResultEvent(Player player, ItemStack result, Container inputs) {
		super(player);

		this.outputStack = result;
		this.newOutputStack = outputStack;
		this.inputs = inputs;
	}

	/**
	 * The originally computed result stack.
	 * <p>
	 * The stack may be empty
	 */
	@NotNull
	public ItemStack getOriginalOutput() {
		return this.outputStack;
	}

	/**
	 * The event-modified output stack
	 * <p>
	 * The stack may be empty
	 */
	@NotNull
	public ItemStack getOutput() {
		return this.newOutputStack;
	}

	/**
	 * Set a new ItemStack for the result slot
	 */
	public void setNewOutput(@NotNull ItemStack stack) {
		this.newOutputStack = stack;
	}

	/**
	 * The input slots of the GrindstoneMenu
	 */
	public Container getInputs() {
		return this.inputs;
	}
}
