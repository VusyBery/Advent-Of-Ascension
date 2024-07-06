package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.util.InventoryUtil;

import java.util.function.Supplier;

public class CrystalCreator extends Block {
	private final Supplier<Item> gemstone;
	private final Supplier<Item> gemtrap;
	private final Supplier<Item> crystal;

	public CrystalCreator(BlockBehaviour.Properties properties, Supplier<Item> gemstone, Supplier<Item> gemtrap, Supplier<Item> crystal) {
		super(properties);

		this.gemstone = gemstone;
		this.gemtrap = gemtrap;
		this.crystal = crystal;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() == gemstone.get() || stack.getItem() == gemtrap.get()) {
			if (player instanceof ServerPlayer pl) {
				pl.setItemInHand(hand, ItemStack.EMPTY);

				InventoryUtil.giveItemTo(pl, new ItemStack(this.crystal.get(), stack.getCount()));
				level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), AoASounds.BLOCK_CRYSTAL_CREATOR_CONVERT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			}

			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
