package net.tslat.aoa3.player.ability.extraction;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.generic.ScalableModAbility;
import net.tslat.aoa3.player.skill.AoASkill;

import java.util.List;

public class HardBlockSpeedIncrease extends ScalableModAbility {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(PlayerEvent.BreakSpeed.class, serverOnly(this::handleHarvestSpeedCheck)));

	public HardBlockSpeedIncrease(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.HARD_BLOCK_SPEED_INCREASE.get(), skill, data);
	}

	public HardBlockSpeedIncrease(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.HARD_BLOCK_SPEED_INCREASE.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleHarvestSpeedCheck(final PlayerEvent.BreakSpeed ev) {
		if (ev.getPosition().isEmpty())
			return;

		float hardness = ev.getState().getDestroySpeed(ev.getEntity().level(), ev.getPosition().get());

		if (hardness > 2)
			ev.setNewSpeed(ev.getNewSpeed() * (1 + (getScaledValue() * ((hardness - 2) / 48f))));
	}
}
