package net.tslat.aoa3.content.block.functional.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.block.AoABlockEntities;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.block.WaterloggableBlock;
import net.tslat.aoa3.content.block.blockentity.TrophyBlockEntity;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.NBTUtil;
import net.tslat.aoa3.util.RegistryUtil;
import net.tslat.aoa3.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class TrophyBlock extends WaterloggableBlock implements EntityBlock {
	private static final VoxelShape FULL_AABB = Shapes.or(
			Block.box(4, 0, 4, 12, 2, 12),
			Block.box(5, 2, 5, 11, 9, 11),
			Block.box(4.5, 9, 4.5, 11.5, 11, 11.5));

	public TrophyBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return FULL_AABB;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrophyBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (blockEntityType != AoABlockEntities.TROPHY.get())
			return null;

		if (!level.isClientSide())
			return null;

		return (entityLevel, entityPos, entityState, blockEntity) -> TrophyBlockEntity.doClientTick(entityLevel, entityPos, entityState, (TrophyBlockEntity)blockEntity);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack heldStack = player.getItemInHand(hand);

		if (!WorldUtil.canModifyBlock(level, pos, player, heldStack))
			return ItemInteractionResult.FAIL;

		if (heldStack.getItem() instanceof SpawnEggItem spawnEgg) {
			if (!level.isClientSide()) {
				if (level.getBlockEntity(pos) instanceof TrophyBlockEntity trophyBlockEntity) {
					trophyBlockEntity.setEntity(spawnEgg.getType(heldStack), true);

					if (!player.getAbilities().instabuild)
						heldStack.shrink(1);
				}
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);

		level.getBlockEntity(pos, AoABlockEntities.TROPHY.get()).ifPresent(trophy -> trophy.saveToItem(stack, level.registryAccess()));

		return stack;
	}

	public static Component getDefaultNameWithEntity(TrophyData trophyData, String trophyId) {
		return LocaleUtil.getLocaleMessage("block.aoa3." + trophyId + ".desc", trophyData.getEntity().getName());
	}

	public static ItemStack cloneTrophy(ItemStack sourceTrophy, ItemLike destTrophy) {
		ItemStack cloneStack = destTrophy.asItem().getDefaultInstance();
		TrophyData trophyData = sourceTrophy.get(AoADataComponents.TROPHY_DATA);

		if (trophyData == null)
			trophyData = new TrophyData(false, new NBTUtil.NBTBuilder<>(new CompoundTag()).putString("id", RegistryUtil.getId(EntityType.ITEM).toString()).build());

		cloneStack.set(AoADataComponents.TROPHY_DATA, trophyData);
		cloneStack.set(DataComponents.CUSTOM_NAME, getDefaultNameWithEntity(trophyData, RegistryUtil.getId(cloneStack).getPath()));

		return cloneStack;
	}

	public static boolean isOriginal(ItemStack stack) {
		TrophyData trophyData = stack.get(AoADataComponents.TROPHY_DATA);

		return trophyData != null && trophyData.isOriginalTrophy();
	}

	public record TrophyData(boolean isOriginalTrophy, CompoundTag entityData, Function<TrophyData, Entity> cachedEntity) {
		public static final Codec<TrophyData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				Codec.BOOL.fieldOf("is_original").forGetter(TrophyData::isOriginalTrophy),
				CompoundTag.CODEC.fieldOf("entity_data").forGetter(TrophyData::entityData)
		).apply(builder, TrophyData::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, TrophyData> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL, TrophyData::isOriginalTrophy,
				ByteBufCodecs.COMPOUND_TAG, TrophyData::entityData,
				TrophyData::new);

		public TrophyData(boolean isOriginalTrophy, CompoundTag entityData) {
			this(isOriginalTrophy, entityData, new Function<>() {
                Entity value = null;

                @Override
                public Entity apply(TrophyData trophyData) {
                    if (this.value == null)
                        this.value = trophyData.createEntity();

                    return this.value;
                }
            });
		}

		public TrophyData(boolean isOriginalTrophy, EntityType<?> entity) {
			this(isOriginalTrophy, new NBTUtil.NBTBuilder<>(new CompoundTag()).putString("id", RegistryUtil.getId(entity).toString()).build());
		}

		public Entity getEntity() {
			return this.cachedEntity.apply(this);
		}

		@Nullable
		public EntityType<?> getEntityType() {
			return AoARegistries.ENTITIES.getEntry(ResourceLocation.read(this.entityData.getString("id")).getOrThrow());
		}

		private Entity createEntity() {
			final Level level = FMLEnvironment.dist == Dist.CLIENT ? ClientOperations.getLevel() : WorldUtil.getServer().overworld();
			Entity entity = EntityType.loadEntityRecursive(entityData(), level, Function.identity());

			if (entity == null)
				entity = new ItemEntity(level, 0, 0, 0, Items.AIR.getDefaultInstance());

			entity.tickCount = 1;

			return entity;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (!(obj instanceof TrophyData otherTrophy))
				return false;

			if (this.isOriginalTrophy ^ otherTrophy.isOriginalTrophy)
				return false;

			return Objects.equals(this.entityData, otherTrophy.entityData);
		}
	}
}
