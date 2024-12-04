package net.tslat.aoa3.content.item.misc.summoning;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import org.jetbrains.annotations.Nullable;


public interface BossTokenItem {
	Entity spawnBoss(ServerLevel level, Vec3 position, ItemStack stack, int playerCount);

	@Nullable
	EntityType<? extends Entity> getEntityType(ItemStack stack);

	@FunctionalInterface
	interface SpawningFunction {
		void spawn(ServerLevel level, Vec3 position, ItemStack stack, int playerCount);
	}

	default AttributeModifier getPerPlayerHealthBuff(int playerCount) {
		return new AttributeModifier(AdventOfAscension.id("per_player_boss_health_buff"), 0.25d * Math.max(1, playerCount - 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}
}
