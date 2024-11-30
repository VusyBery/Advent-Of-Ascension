package net.tslat.aoa3.player.ability.hauling;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.HaulingRodPullEntityEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.generic.ScalableModAbility;
import net.tslat.aoa3.player.skill.AoASkill;

import java.util.List;

public class HaulingRodPullStrengthModifier extends ScalableModAbility {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(HaulingRodPullEntityEvent.class, serverOnly(this::handleHaulingRodPullEntity)));

	public HaulingRodPullStrengthModifier(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.HAULING_ROD_PULL_STRENGTH.get(), skill, data);
	}

	public HaulingRodPullStrengthModifier(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.HAULING_ROD_PULL_STRENGTH.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleHaulingRodPullEntity(HaulingRodPullEntityEvent ev) {
		ev.setPullStrength(ev.getPullStrength() * (1 + getScaledValue()));
	}
}
