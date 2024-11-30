package net.tslat.aoa3.player.skill;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.List;

public class DexteritySkill extends AoASkill.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(PlayerTickEvent.Pre.class, PlayerTickEvent.Pre::getEntity, serverOnly(this::handlePlayerTick)),
			listener(LivingEvent.LivingJumpEvent.class, LivingEvent.LivingJumpEvent::getEntity, serverOnly(this::handlePlayerJump)),
			listener(CriticalHitEvent.class, CriticalHitEvent::getEntity, serverOnly(this::handleCriticalHit)));

	private double lastX = 0;
	private double lastZ = 0;
	private double cumulativeDistance = 0;

	private float cumulativeXp = 0;

	public DexteritySkill(ServerPlayerDataManager plData, JsonObject jsonData) {
		super(AoASkills.DEXTERITY.get(), plData, jsonData);
	}

	public DexteritySkill(CompoundTag nbtData) {
		super(AoASkills.DEXTERITY.get(), nbtData);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handlePlayerTick(final PlayerTickEvent.Pre ev) {
		if (!canGainXp(true) || ev.getEntity().isPassenger())
			return;

		final Player pl = ev.getEntity();
		final Vec3 pos = pl.position();

		if (pl.isSprinting()) {
			if (pl.onGround() || pl.isSwimming()) {
				if (lastX != 0 && lastZ != 0) {
					double distX = pos.x() - this.lastX;
					double distZ = pos.z() - this.lastZ;
					double dist = Math.sqrt(distX * distX + distZ * distZ);

					if (dist < 1)
						cumulativeDistance += dist;
				}

				this.lastX = pos.x();
				this.lastZ = pos.z();
			}
		}
		else {
			this.lastX = 0;
			this.lastZ = 0;
		}

		if (pl.tickCount % 200 == 0) {
			if (cumulativeDistance > 0) {
				cumulativeXp += PlayerUtil.getTimeBasedXpForLevel(getLevel(true), 100) * Math.min(1.65f, (float)(cumulativeDistance / 56f));
				cumulativeDistance = 0;
			}

			if (cumulativeXp > 0) {
				adjustXp(cumulativeXp, false, false);

				cumulativeXp = 0;
			}
		}
	}

	private void handlePlayerJump(final LivingEvent.LivingJumpEvent ev) {
		if (!canGainXp(true))
			return;

		float xp = PlayerUtil.getTimeBasedXpForLevel(getLevel(true), 5);

		if (ev.getEntity().isSprinting())
			xp *= 1.3f;

		cumulativeXp += xp;
	}

	private void handleCriticalHit(final CriticalHitEvent ev) {
		if (!canGainXp(true))
			return;

		cumulativeXp += ev.getDamageMultiplier() * PlayerUtil.getTimeBasedXpForLevel(getLevel(true), 12);
	}
}
