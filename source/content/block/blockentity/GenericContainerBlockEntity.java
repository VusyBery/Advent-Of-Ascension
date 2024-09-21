package net.tslat.aoa3.content.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GenericContainerBlockEntity extends BaseContainerBlockEntity {
    public GenericContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract IItemHandler getItemHandler();
    public abstract CompoundTag saveContents(HolderLookup.Provider holderLookup);
    public abstract void loadContents(HolderLookup.Provider holderLookup, CompoundTag compound);
    @Override public abstract void clearContent();
    public abstract void setItemNoUpdate(int slot, ItemStack stack);

    @Override
    public void setItem(int slot, ItemStack stack) {
        setItemNoUpdate(slot, stack);
        markUpdated();
    }

    public void dropContents() {
        Vec3 pos = Vec3.atCenterOf(getBlockPos());

        for (ItemStack stack : getItems()) {
            this.level.addFreshEntity(new ItemEntity(this.level, pos.x, pos.y, pos.z, stack));
        }

        clearContent();
    }

    @Override
    protected final void setItems(NonNullList<ItemStack> items) {
        setItems((List<ItemStack>)items);
    }

    protected void setItems(List<ItemStack> items) {
        clearContent();

        int maxSize = getContainerSize();

        for (int i = 0; i < maxSize && i < items.size(); i++) {
            setItemNoUpdate(i, items.get(i));
        }

        markUpdated();
    }

    public void markUpdated() {
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider holderLookup) {
        CompoundTag compound = super.getUpdateTag(holderLookup);

        compound.put("Contents", saveContents(holderLookup));

        return compound;
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider holderLookup) {
        super.saveAdditional(compound, holderLookup);

        compound.put("Contents", saveContents(holderLookup));
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider holderLookup) {
        super.loadAdditional(compound, holderLookup);

        loadContents(holderLookup, compound);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
        tag.remove("Lock");
        tag.remove("Contents");
    }
}
