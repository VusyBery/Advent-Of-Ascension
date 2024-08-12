package net.tslat.aoa3.content.entity.animal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.common.registration.entity.AoAEntitySpawnPlacements;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.util.PlayerUtil;

public class ShinySquidEntity extends Squid {
	public ShinySquidEntity(EntityType<? extends ShinySquidEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);

		if (!isRemoved() && dead) {
			Entity killer = source.getEntity();

			if (killer instanceof ServerPlayer player && player.fishing != null && player.fishing.getHookedIn() == this)
				PlayerUtil.givePartialLevelToPlayer(player, AoASkills.HAULING.get(), 0.1f, false);
		}
	}

	public static SpawnPlacements.SpawnPredicate<LivingEntity> spawnRules() {
		return new AoAEntitySpawnPlacements.SpawnBuilder<>(GlowSquid::checkGlowSquidSpawnRules).spawnChance(1 / 1000f);
	}

	public static AoAEntityStats.AttributeBuilder entityStats(EntityType<ShinySquidEntity> entityType) {
		return AoAEntityStats.AttributeBuilder.createMonster(entityType)
				.health(15)
				.swimSpeedMod(1.1f)
				.followRange(16);
	}
}
