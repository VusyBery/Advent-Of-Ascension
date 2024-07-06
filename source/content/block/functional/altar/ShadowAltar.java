package net.tslat.aoa3.content.block.functional.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.scheduling.async.ShadowlordSpawnTask;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.concurrent.TimeUnit;

public class ShadowAltar extends BossAltarBlock {
	public ShadowAltar(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected void doActivationEffect(Player player, InteractionHand hand, BlockState state, BlockPos blockPos) {
		if (player instanceof ServerPlayer pl) {
			new ShadowlordSpawnTask(pl, blockPos).schedule(1, TimeUnit.SECONDS);

			if (player.hasEffect(MobEffects.NIGHT_VISION) && InventoryUtil.findItemForConsumption(pl, AoAItems.BLANK_REALMSTONE, pl.getAbilities().instabuild ? 0 : 1, true))
				InventoryUtil.giveItemTo(pl, AoAItems.DUSTOPIA_REALMSTONE);
		}
	}

	@Override
	protected boolean checkActivationConditions(Player player, InteractionHand hand, BlockState state, BlockPos pos) {
		return WorldUtil.isWorld(player.level(), AoADimensions.ABYSS);
	}

	@Override
	protected Item getActivationItem() {
		return AoAItems.BOOK_OF_SHADOWS.get();
	}
}
