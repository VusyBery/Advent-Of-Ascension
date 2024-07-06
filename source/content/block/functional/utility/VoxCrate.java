package net.tslat.aoa3.content.block.functional.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.aoa3.common.registration.entity.AoANpcs;
import net.tslat.aoa3.content.entity.npc.trader.LottomanEntity;
import net.tslat.aoa3.util.EnchantmentUtil;
import net.tslat.aoa3.util.LocaleUtil;

public class VoxCrate extends Block {
	public VoxCrate(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(level, pos, state, player);

		if (!level.isClientSide && !EnchantmentUtil.hasEnchantment(level, player.getMainHandItem(), Enchantments.SILK_TOUCH)) {
			LottomanEntity lottoman = new LottomanEntity(AoANpcs.LOTTOMAN.get(), level);

			lottoman.moveTo(pos.getX(), pos.getY() + 0.5, pos.getZ(), 0, 0);
			lottoman.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
			level.addFreshEntity(lottoman);
			player.sendSystemMessage(LocaleUtil.getLocaleMessage(AoANpcs.LOTTOMAN.get().getDescriptionId() + ".spawn"));
		}

		return state;
	}
}
