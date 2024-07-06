package net.tslat.aoa3.player.ability.innervation;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tslat.aoa3.client.AoAKeybinds;
import net.tslat.aoa3.client.player.AoAPlayerKeybindListener;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.player.AoAPlayerEventListener;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.NumberUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.effectslib.api.util.EffectBuilder;

import java.util.function.Consumer;

public class StunStrike extends AoAAbility.Instance {
	private static final ListenerType[] LISTENERS = new ListenerType[] {ListenerType.KEY_INPUT, ListenerType.OUTGOING_ATTACK_AFTER};

	private final float energyCost;
	private final int stunDuration;

	private boolean primedAttack = false;

	public StunStrike(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.STUN_STRIKE.get(), skill, data);

		this.energyCost = GsonHelper.getAsFloat(data, "energy_cost");
		this.stunDuration = GsonHelper.getAsInt(data, "stun_duration", 20);
	}

	public StunStrike(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.STUN_STRIKE.get(), skill, data);

		this.energyCost = data.getFloat("energy_cost");
		this.stunDuration = data.getInt("stun_duration");
	}

	@Override
	protected void updateDescription(MutableComponent defaultDescription) {
		super.updateDescription(Component.translatable(((TranslatableContents)defaultDescription.getContents()).getKey(), NumberUtil.roundToNthDecimalPlace(this.stunDuration / 20f, 2), NumberUtil.roundToNthDecimalPlace(this.energyCost, 2)));
	}

	@Override
	public ListenerType[] getListenerTypes() {
		return LISTENERS;
	}

	@Override
	public void createKeybindListener(Consumer<AoAPlayerKeybindListener> consumer) {
		consumer.accept(new AoAPlayerKeybindListener() {
			@Override
			public AoAPlayerEventListener getEventListener() {
				return StunStrike.this;
			}

			@Override
			public int getKeycode() {
				return AoAKeybinds.ABILITY_ACTION.getKey().getValue();
			}

			@Override
			public boolean shouldSendKeyPress() {
				LocalPlayer pl = Minecraft.getInstance().player;

				return !pl.input.hasForwardImpulse() && pl.input.leftImpulse == 0 && !pl.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && PlayerUtil.getResourceValue(pl, AoAResources.ENERGY.get()) >= StunStrike.this.energyCost;
			}
		});
	}

	@Override
	public void handleKeyInput() {
		ServerPlayer player = (ServerPlayer)getPlayer();

		if (!primedAttack && skill.getPlayerDataManager().getResource(AoAResources.ENERGY.get()).hasAmount(this.energyCost)) {
			this.primedAttack = true;

			activatedActionKey(player);
		}
	}

	@Override
	public void handleAfterAttacking(LivingDamageEvent.Post ev) {
		if (ev.getNewDamage() > 0 && primedAttack && DamageUtil.isMeleeDamage(ev.getSource()) && !getPlayer().getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
			this.primedAttack = false;

			if (skill.getPlayerDataManager().getResource(AoAResources.ENERGY.get()).consume(this.energyCost, true)) {
				ServerPlayer player = (ServerPlayer)getPlayer();
				LivingEntity target = ev.getEntity();

				AoAScheduler.scheduleSyncronisedTask(() -> {
					player.swing(InteractionHand.OFF_HAND, true);

					if (target != null && target.isAlive()) {
						if (player.distanceToSqr(target) < 36 && player.hasLineOfSight(target)) {
							DamageUtil.doScaledKnockback(target, player, 0.3f, 1, 1, 1);
							EntityUtil.applyPotions(target, new EffectBuilder(MobEffects.MOVEMENT_SLOWDOWN, this.stunDuration).level(127), new EffectBuilder(MobEffects.DIG_SLOWDOWN, this.stunDuration).level(127));
							activatedActionKey(player);

							if (skill.canGainXp(true))
								PlayerUtil.giveTimeBasedXpToPlayer(player, this.skill.type(), 39,  false);
						}
					}
				}, player.getCurrentSwingDuration() - 1);
			}
		}
	}

	@Override
	public CompoundTag getSyncData(boolean forClientSetup) {
		CompoundTag data = super.getSyncData(forClientSetup);

		if (forClientSetup) {
			data.putFloat("energy_cost", this.energyCost);
			data.putFloat("stun_duration", this.stunDuration);
		}

		return data;
	}
}
