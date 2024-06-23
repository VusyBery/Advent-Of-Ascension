package net.tslat.aoa3.content.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.menu.InfusionTableMenu;
import net.tslat.aoa3.common.registration.block.AoABlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusionTableBlockEntity extends BlockEntity implements Nameable, MenuProvider {
	private static final Component DEFAULT_NAME = Component.translatable("container." + AdventOfAscension.MOD_ID + ".infusion_table");

	private final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);

	@Nullable
	private Component customName;

	public InfusionTableBlockEntity(BlockPos pos, BlockState state) {
		super(AoABlockEntities.INFUSION_TABLE.get(), pos, state);
	}

	public NonNullList<ItemStack> getContents() {
		return this.items;
	}

	public void dropContents(Level world, BlockPos pos) {
		NonNullList<ItemStack> contents = getContents();

		for (int i = 0; i < contents.size() - 1; i++) {
			world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, contents.get(i)));
		}

		this.items.clear();
		setChanged();
	}

	public void setContents(List<ItemStack> contents) {
		this.items.clear();

		for (int i = 0; i < 10 && contents.size() > i; i++) {
			this.items.set(i, contents.get(i));
		}

		markUpdated();
	}

	public void setOutput(ItemStack itemStack) {
		this.items.set(10, itemStack);
		markUpdated();
	}

	private void markUpdated() {
		this.setChanged();
		this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag tag = super.getUpdateTag(registryLookup);

		ContainerHelper.saveAllItems(tag, this.items, registryLookup);

		return tag;
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registryLookup) {
		super.saveAdditional(compound, registryLookup);

		ContainerHelper.saveAllItems(compound, this.items, registryLookup);

		if (this.customName != null)
			compound.putString("CustomName", Component.Serializer.toJson(this.customName, registryLookup));
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registryLookup) {
		super.loadAdditional(compound, registryLookup);

		this.items.clear();
		ContainerHelper.loadAllItems(compound, this.items, registryLookup);

		this.customName = compound.contains("CustomName", Tag.TAG_STRING) ? parseCustomNameSafe(compound.getString("CustomName"), registryLookup) : null;
	}

	@Override
	public Component getName() {
		return this.customName != null ? this.customName : DEFAULT_NAME;
	}

	@Override
	public Component getDisplayName() {
		return getName();
	}

	public void setCustomName(@Nullable Component name) {
		this.customName = name;
	}

	@Nullable
	public Component getCustomName() {
		return this.customName;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Nullable
	@Override
	public InfusionTableMenu createMenu(int containerId, Inventory playerInventory, Player openingPlayer) {
		InfusionTableMenu container = new InfusionTableMenu(containerId, playerInventory, ContainerLevelAccess.create(getLevel(), getBlockPos())) {
			@Override
			protected void clearContainer(Player player, Container container) {
				setContents(getInventory().getItems());
				setOutput(getOutputItem());
				getInventory().clearContent();

				super.clearContainer(player, container);
			}

			@Override
			public void slotsChanged(Container inventory) {
				if (openingPlayer.containerMenu == this) {
					setContents(getInventory().getItems());
					setOutput(getOutputItem());
				}

				super.slotsChanged(inventory);
			}
		};

		NonNullList<ItemStack> contents = getContents();

		for (int i = 0; i < 10 && i < contents.size(); i++) {
			container.getInventory().setItem(i, contents.get(i));
		}

		return container;
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput components) {
		super.applyImplicitComponents(components);

		setCustomName(components.get(DataComponents.CUSTOM_NAME));
		components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(getContents());
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(DataComponents.CUSTOM_NAME, this.customName);
		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getContents()));
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CustomName");
		tag.remove("Items");
	}
}
