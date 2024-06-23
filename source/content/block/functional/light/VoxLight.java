package net.tslat.aoa3.content.block.functional.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class VoxLight extends Block {
	public VoxLight(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == AoAItems.ACTIVE_RUNE_STONE.get() && WorldUtil.isWorld(level, AoADimensions.MYSTERIUM)) {
			if (!level.isClientSide) {
				List<ItemEntity> itemsList = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));

				if (itemsList.size() > 1) {
					ItemEntity realmstone = null;
					ItemEntity runicEnergy = null;

					for (ItemEntity entity : itemsList) {
						Item item = entity.getItem().getItem();

						if (item == AoAItems.BLANK_REALMSTONE.get()) {
							realmstone = entity;
						}
						else if (item == AoAItems.RUNIC_ENERGY.get()) {
							runicEnergy = entity;
						}

						if (realmstone != null && runicEnergy != null) {
							player.getItemInHand(hand).shrink(1);
							realmstone.setItem(new ItemStack(AoAItems.RUNANDOR_REALMSTONE.get()));
							runicEnergy.discard();
						}
					}
				}
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
