package net.tslat.aoa3.player.ability.hauling;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.content.skill.hauling.HaulingSpawnPool;
import net.tslat.aoa3.event.custom.events.HaulingItemFishedEvent;
import net.tslat.aoa3.event.custom.events.PlayerChangeXpEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.generic.ScalableModAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.NumberUtil;

import java.util.List;

public class FishingTrapSpawn extends ScalableModAbility {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(ItemFishedEvent.class, serverOnly(this::handleItemFished)),
			listener(PlayerChangeXpEvent.class, serverOnly(this::handleSkillXpGain)));

	public FishingTrapSpawn(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.FISHING_TRAP_SPAWN.get(), skill, data);
	}

	public FishingTrapSpawn(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.FISHING_TRAP_SPAWN.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	@Override
	protected MutableComponent getScalingDescriptionComponent(int precision) {
		return LocaleUtil.getAbilityValueDesc(baseValue != 0, perLevelMod != 0, isPercent(),
				NumberUtil.roundToNthDecimalPlace(baseValue * (isPercent() ? 100 : 1), precision),
				NumberUtil.roundToNthDecimalPlace(perLevelMod * (isPercent() ? 100 : 1), precision),
				NumberUtil.roundToNthDecimalPlace(Math.max(0, getScaledValue() * (isPercent() ? 100 : 1)), precision));
	}

	private void handleItemFished(ItemFishedEvent ev) {
		if (ev.getEntity() instanceof ServerPlayer pl && testAsChance()) {
			FishingHook bobber = ev.getHookEntity();
			Level level = bobber.level();
			BlockPos pos = bobber.blockPosition();
			float luck = bobber.luck;
			boolean isLava = false;

			if (ev instanceof HaulingItemFishedEvent haulingEv) {
				luck = haulingEv.getLuck();
				isLava = Fluids.LAVA.is(haulingEv.getHookEntity().getApplicableFluid());
			}

			float finalLuck = luck;
			boolean finalLava = isLava;

			HaulingSpawnPool.getTrapsPoolForLocation(level, pos, finalLava ? NeoForgeMod.LAVA_TYPE.value() : NeoForgeMod.WATER_TYPE.value())
					.flatMap(pool -> pool.getEntry(pl, finalLuck))
					.map(haulingEntity -> haulingEntity.apply(level, finalLava))
					.ifPresent(trapEntity -> {
						double velX = pl.getX() - bobber.getX();
						double velY = pl.getY() - bobber.getY();
						double velZ = pl.getZ() - bobber.getZ();

						trapEntity.setDeltaMovement(velX * 0.1d, velY * 0.1d + Math.sqrt(Math.sqrt(velX * velX + velY * velY + velZ * velZ)) * 0.15d, velZ * 0.1d);
						trapEntity.setPos(bobber.getX(), bobber.getY(), bobber.getZ());
						level.addFreshEntity(trapEntity);
					});
		}
	}

	private void handleSkillXpGain(PlayerChangeXpEvent ev) {
		if (ev.getSkill().type() == AoASkills.HAULING.get())
			ev.setXpGain(ev.getNewXpGain() * 1.1f);
	}
}
