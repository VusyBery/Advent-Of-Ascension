package net.tslat.aoa3.content.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.block.AoABlockEntities;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.block.functional.misc.TrophyBlock;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.RegistryUtil;
import org.jetbrains.annotations.Nullable;

public class TrophyBlockEntity extends BlockEntity implements Nameable {
	private static final Component DEFAULT_NAME = LocaleUtil.getLocaleMessage("block.aoa3.trophy");

	@Nullable
	private TrophyBlock.TrophyData trophyData;

	@Nullable
	private Component customName;

	private float mobRotation;
	private float prevMobRotation;
	public double hoverStep;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(AoABlockEntities.TROPHY.get(), pos, state);
	}

	public float getMobRotation() {
		return mobRotation;
	}

	public float getPrevMobRotation() {
		return prevMobRotation;
	}

	@Nullable
	public TrophyBlock.TrophyData getTrophyData() {
		return this.trophyData;
	}

	public void setEntity(EntityType<?> entity, boolean isEgg) {
		this.trophyData = new TrophyBlock.TrophyData(!isEgg, entity);

		if (this.customName == null)
			setCustomName(TrophyBlock.getDefaultNameWithEntity(this.trophyData, RegistryUtil.getId(getBlockState()).getPath()));

		markUpdated();
	}

	public static void doClientTick(Level level, BlockPos pos, BlockState state, TrophyBlockEntity blockEntity) {
		blockEntity.prevMobRotation = blockEntity.mobRotation;
		blockEntity.mobRotation = (blockEntity.mobRotation + 0.05f) % 360;
	}

	private void markUpdated() {
		this.setChanged();
		this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveCustomOnly(registryLookup);
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registryLookup) {
		super.saveAdditional(compound, registryLookup);

		if (this.trophyData != null) {
			CompoundTag trophyTag = new CompoundTag();

			trophyTag.putBoolean("is_original", this.trophyData.isOriginalTrophy());
			trophyTag.put("entity_data", this.trophyData.entityData());
			compound.put("TrophyData", trophyTag);
		}

		if (this.customName != null)
			compound.putString("CustomName", Component.Serializer.toJson(this.customName, registryLookup));
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registryLookup) {
		super.loadAdditional(compound, registryLookup);

		CompoundTag trophyTag = compound.getCompound("TrophyData");
		this.trophyData = trophyTag.isEmpty() ? null : new TrophyBlock.TrophyData(trophyTag.getBoolean("is_original"), trophyTag.getCompound("entity_data"));
		this.customName = compound.contains("CustomName", Tag.TAG_STRING) ? parseCustomNameSafe(compound.getString("CustomName"), registryLookup) : null;
	}

	@Nullable
	public Entity getCachedEntity() {
		return this.trophyData != null ? this.trophyData.getEntity() : null;
	}

	@Override
	public Component getName() {
		return this.customName != null ? this.customName : DEFAULT_NAME;
	}

	public void setCustomName(@Nullable Component name) {
		this.customName = name;
	}

	@Override
	public Component getDisplayName() {
		return Nameable.super.getDisplayName();
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return this.customName;
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput components) {
		super.applyImplicitComponents(components);

		this.trophyData = components.get(AoADataComponents.TROPHY_DATA);
		setCustomName(components.get(DataComponents.CUSTOM_NAME));
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(AoADataComponents.TROPHY_DATA, this.trophyData);
		builder.set(DataComponents.CUSTOM_NAME, this.customName);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("TrophyData");
		tag.remove("CustomName");
	}
}
