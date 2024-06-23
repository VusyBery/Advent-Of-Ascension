package net.tslat.aoa3.content.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.block.AoABlockEntities;
import net.tslat.aoa3.util.RegistryUtil;
import org.jetbrains.annotations.Nullable;


public class BossAltarBlockEntity extends BlockEntity {
	private EntityType<?> entityType = null;
	private Entity cachedEntity = null;

	public BossAltarBlockEntity(BlockPos pos, BlockState blockState) {
		super(AoABlockEntities.BOSS_ALTAR.get(), pos, blockState);
	}

	public void updateEntity(@Nullable EntityType<?> entityType) {
		this.entityType = entityType;

		if (level != null)
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		CompoundTag tag = super.getUpdateTag(registryLookup);

		tag.putString("entityType", this.entityType == null ? "" : RegistryUtil.getId(this.entityType).toString());

		return tag;
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);

		if (tag.contains("entityType")) {
			String entityTypeString = tag.getString("entityType");

			if (entityTypeString.isEmpty()) {
				this.entityType = null;
			}
			else {
				this.entityType = AoARegistries.ENTITIES.getEntry(ResourceLocation.read(entityTypeString).getOrThrow());
			}

			if (this.cachedEntity != null)
				this.cachedEntity.discard();

			this.cachedEntity = this.entityType == null || this.level == null ? null : this.entityType.create(this.level);
		}
	}

	@Nullable
	public EntityType<?> getCurrentEntity() {
		return this.entityType;
	}

	@Nullable
	public Entity getCachedEntity() {
		return this.cachedEntity;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
