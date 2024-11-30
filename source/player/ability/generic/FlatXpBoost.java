package net.tslat.aoa3.player.ability.generic;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.GsonHelper;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.PlayerChangeXpEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.NumberUtil;

import java.util.List;

public class FlatXpBoost extends AoAAbility.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(PlayerChangeXpEvent.class, serverOnly(this::handleSkillXpGain)));

	private final float modifier;

	public FlatXpBoost(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.FLAT_XP_BOOST.get(), skill, data);

		this.modifier = GsonHelper.getAsFloat(data, "modifier");
	}

	public FlatXpBoost(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.FLAT_XP_BOOST.get(), skill, data);

		this.modifier = data.getFloat("modifier");
	}

	@Override
	protected void updateDescription(MutableComponent defaultDescription) {
		super.updateDescription(Component.translatable(((TranslatableContents)defaultDescription.getContents()).getKey(),
				skill.getName(),
				NumberUtil.roundToNthDecimalPlace(modifier - 1f, 2)));
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleSkillXpGain(PlayerChangeXpEvent ev) {
		ev.setXpGain(ev.getNewXpGain() * modifier);
	}

	@Override
	public CompoundTag getSyncData(boolean forClientSetup) {
		CompoundTag data = super.getSyncData(forClientSetup);

		if (forClientSetup)
			data.putFloat("modifier", this.modifier);

		return data;
	}
}
