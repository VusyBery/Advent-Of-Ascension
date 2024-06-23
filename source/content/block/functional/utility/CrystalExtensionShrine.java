package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.List;

public class CrystalExtensionShrine extends Block {
	public CrystalExtensionShrine(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!player.getItemInHand(hand).isEmpty()) {
			if (stack.getItem() == AoAItems.RAINBOW_DRUSE.get()) {
				if (player instanceof ServerPlayer) {
					List<ItemEntity> crystalList = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.getX() - 5, pos.getY() - 1, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 1, pos.getZ() + 5), entity -> isCrystal(entity.getItem().getItem()));
					int count = 0;

					for (ItemEntity item : crystalList) {
						count += item.getItem().getCount();

						if (count >= 10)
							break;
					}

					if (count < 10) {
						PlayerUtil.notifyPlayer(player, Component.translatable(LocaleUtil.createFeedbackLocaleKey("crystalExtensionShrine.crystals")));

						return ItemInteractionResult.FAIL;
					}

					for (int i = 10; i > 0; i--) {
						ItemEntity entity = crystalList.getFirst();
						ItemStack entityStack = entity.getItem();
						int size = entityStack.getCount();

						entityStack.shrink(i);

						if (entityStack.getCount() <= 0)
							crystalList.removeFirst();

						i -= size - 1;
					}

					if (!player.isCreative())
						stack.shrink(1);

					ItemUtil.givePlayerItemOrDrop(player, new ItemStack(AoAItems.GIANT_CRYSTAL.get()));
				}

				return ItemInteractionResult.sidedSuccess(level.isClientSide);
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	private static boolean isCrystal(Item item) {
		return item == AoAItems.BLUE_CRYSTAL.get() || item == AoAItems.GREEN_CRYSTAL.get() || item == AoAItems.PURPLE_CRYSTAL.get() || item == AoAItems.RED_CRYSTAL.get() || item == AoAItems.WHITE_CRYSTAL.get() || item == AoAItems.YELLOW_CRYSTAL.get();
	}
}
