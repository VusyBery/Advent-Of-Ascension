package net.tslat.aoa3.content.block.functional.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.item.misc.summoning.BossTokenItem;
import net.tslat.aoa3.util.EntitySpawningUtil;
import org.jetbrains.annotations.Nullable;

public class GoldTrophyBlock extends TrophyBlock implements BossTokenItem {
	public GoldTrophyBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public Entity spawnBoss(ServerLevel level, Vec3 position, ItemStack itemStack) {
		return EntitySpawningUtil.spawnEntity(level, getEntityType(itemStack), position, MobSpawnType.TRIGGERED);
	}

	@Nullable
	@Override
	public EntityType<?> getEntityType(ItemStack stack) {
		TrophyData trophyData = stack.get(AoADataComponents.TROPHY_DATA);

		if (trophyData == null || !trophyData.isOriginalTrophy())
			return null;

		return trophyData.getEntityType();
	}
}
