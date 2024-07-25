package net.tslat.aoa3.player.ability.generic;

import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.PlayerLevelChangeEvent;
import net.tslat.aoa3.library.object.Text;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.*;

import static net.tslat.aoa3.player.AoAPlayerEventListener.ListenerType.ATTRIBUTE_MODIFIERS;
import static net.tslat.aoa3.player.AoAPlayerEventListener.ListenerType.LEVEL_CHANGE;

public class AttributeModification extends ScalableModAbility {
	private static final ListenerType[] LISTENERS = new ListenerType[] {ATTRIBUTE_MODIFIERS, LEVEL_CHANGE};

	private final Holder<Attribute> attribute;
	private AttributeModifier modifier;

	private float loginHealth = -1;
	private int lastUpdateLevel = 0;

	public AttributeModification(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.ATTRIBUTE_MODIFICATION.get(), skill, data);

		this.attribute = AoARegistries.ENTITY_ATTRIBUTES.getHolder(ResourceLocation.read(GsonHelper.getAsString(data, "attribute")).getOrThrow());
		this.modifier = new AttributeModifier(RegistryUtil.getId(this.type()).withSuffix(getUniqueIdentifier()), getScaledValue(), AttributeModifier.Operation.BY_ID.apply(GsonHelper.getAsInt(data, "operation")));
	}

	public AttributeModification(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.ATTRIBUTE_MODIFICATION.get(), skill, data);

		this.attribute = AoARegistries.ENTITY_ATTRIBUTES.getHolder(ResourceLocation.read(data.getString("attribute")).getOrThrow());
		this.modifier = new AttributeModifier(RegistryUtil.getId(this.type()).withSuffix(getUniqueIdentifier()), getScaledValue(), AttributeModifier.Operation.BY_ID.apply(data.getInt("operation")));
	}

	protected AttributeModifier updateModifier() {
		markForClientSync();

		return this.modifier = new AttributeModifier(this.modifier.id(), getScaledValue(), this.modifier.operation());
	}

	@Override
	protected void updateDescription(MutableComponent defaultDescription) {
		String amount = "";
		String perLevel = "";

		switch (this.modifier.operation()) {
			case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> {
				if (baseValue != 0)
					amount = "+" + NumberUtil.roundToNthDecimalPlace(baseValue * 100, 3);
				if (perLevelMod != 0)
					perLevel = NumberUtil.roundToNthDecimalPlace(this.perLevelMod * 100, 3);
			}
			default -> {
				if (baseValue != 0)
					amount = NumberUtil.roundToNthDecimalPlace(baseValue, 3);
				if (perLevelMod != 0)
					perLevel = NumberUtil.roundToNthDecimalPlace(this.perLevelMod, 3);
			}
		}

		super.updateDescription(Component.translatable(((TranslatableContents)defaultDescription.getContents()).getKey(),
				Text.of(this.attribute.value().getDescriptionId()),
				LocaleUtil.getAbilityValueDesc(baseValue != 0, perLevelMod != 0, modifier.operation() != AttributeModifier.Operation.ADD_VALUE, amount, perLevel, NumberUtil.roundToNthDecimalPlace((float)modifier.amount() * (modifier.operation() == AttributeModifier.Operation.ADD_VALUE ? 1 : 100), 3))));
	}

	@Override
	public MutableComponent getDescription() {
		if (this.skill.getLevel(true) != lastUpdateLevel) {
			this.lastUpdateLevel = this.skill.getLevel(true);

			updateDescription(Component.translatable(Util.makeDescriptionId("ability", AoARegistries.AOA_ABILITIES.getKey(type())) + ".description"));
		}

		return super.getDescription();
	}

	@Override
	public ListenerType[] getListenerTypes() {
		return LISTENERS;
	}

	@Override
	public void applyAttributeModifiers(ServerPlayerDataManager plData) {
		AttributeUtil.applyTransientModifier(plData.player(), this.attribute, this.modifier);

		if (loginHealth > 0) {
			plData.player().setHealth(loginHealth);
			loginHealth = -1;
		}
	}

	@Override
	public void removeAttributeModifiers(ServerPlayerDataManager plData) {
		AttributeUtil.removeModifier(plData.player(), this.attribute, this.modifier.id());
	}

	@Override
	public void handleLevelChange(PlayerLevelChangeEvent ev) {
		AttributeUtil.applyTransientModifier(ev.getEntity(), this.attribute, updateModifier());
	}

	@Override
	public CompoundTag getSyncData(boolean forClientSetup) {
		CompoundTag data = super.getSyncData(forClientSetup);

		if (forClientSetup) {
			data.putString("attribute", RegistryUtil.getId(this.attribute.value()).toString());
			data.putInt("operation", this.modifier.operation().id());
		}

		return data;
	}

	@Override
	public CompoundTag saveToNbt() {
		CompoundTag data = super.saveToNbt();

		if (attribute == Attributes.MAX_HEALTH) {
			double health = getPlayer().getHealth();

			if (health == 0 && !getPlayer().isAlive())
				health = getPlayer().getMaxHealth();

			if (health > 0)
				data.putDouble("current_health", health);
		}

		return data;
	}

	@Override
	public void receiveSyncData(CompoundTag data) {
		super.receiveSyncData(data);

		updateModifier();
	}

	@Override
	public void loadFromNbt(CompoundTag data) {
		super.loadFromNbt(data);

		updateModifier();

		if (attribute == Attributes.MAX_HEALTH && getListenerState() == ListenerState.ACTIVE && data.contains("current_health")) {
			if (getLevelReq() == 1) {
				loginHealth = (float)data.getDouble("current_health");
			}
			else {
				getPlayer().setHealth((float)data.getDouble("current_health"));
			}
		}
	}
}
