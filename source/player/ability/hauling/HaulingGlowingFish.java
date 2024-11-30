package net.tslat.aoa3.player.ability.hauling;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.HaulingSpawnEntityEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;

import java.util.List;

public class HaulingGlowingFish extends AoAAbility.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(HaulingSpawnEntityEvent.class, serverOnly(this::handleHaulingEntitySpawn)));

	public HaulingGlowingFish(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.HAULING_GLOWING_FISH.get(), skill, data);
	}

	public HaulingGlowingFish(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.HAULING_GLOWING_FISH.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleHaulingEntitySpawn(final HaulingSpawnEntityEvent ev) {
		if (ev.getNewEntity() != null)
			ev.getNewEntity().setGlowingTag(true);
	}
}
