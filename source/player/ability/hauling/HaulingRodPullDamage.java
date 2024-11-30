package net.tslat.aoa3.player.ability.hauling;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.common.registration.entity.AoADamageTypes;
import net.tslat.aoa3.event.custom.events.HaulingRodPullEntityEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.generic.ScalableModAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.DamageUtil;

import java.util.List;

public class HaulingRodPullDamage extends ScalableModAbility {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(HaulingRodPullEntityEvent.class, serverOnly(this::handleHaulingRodPullEntity)));

	public HaulingRodPullDamage(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.HAULING_ROD_PULL_DAMAGE.get(), skill, data);
	}

	public HaulingRodPullDamage(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.HAULING_ROD_PULL_DAMAGE.get(), skill, data);
	}

	@Override
	protected boolean isPercent() {
		return false;
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleHaulingRodPullEntity(final HaulingRodPullEntityEvent ev) {
		if (ev.getHookedEntity() instanceof LivingEntity entity && !entity.level().isClientSide)
			DamageUtil.safelyDealDamage(DamageUtil.indirectEntityDamage(AoADamageTypes.HAULING, getPlayer(), ev.getBobber()), entity, getScaledValue());
	}
}
