package net.tslat.aoa3.player.ability.generic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.tslat.aoa3.common.menu.ImbuingChamberMenu;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.ItemCraftingEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.EnchantmentUtil;

import java.util.List;
import java.util.Map;

public class AutoEnchantCrafting extends ScalableModAbility {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(ItemCraftingEvent.class, serverOnly(this::handleItemCrafting)));

	private final ObjectIntPair<Holder<Enchantment>>[] enchantments;

	public AutoEnchantCrafting(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.AUTO_ENCHANT_CRAFTING.get(), skill, data);

		JsonObject enchantMap = data.getAsJsonObject("enchantments");
		this.enchantments = new ObjectIntPair[enchantMap.size()];
		int i = 0;

		for (Map.Entry<String, JsonElement> entry : enchantMap.entrySet()) {
			enchantments[i] = ObjectIntPair.of(EnchantmentUtil.toHolder(getPlayer().level(), ResourceLocation.read(entry.getKey()).getOrThrow()), entry.getValue().getAsInt());
			i++;
		}

		if (enchantments.length == 0)
			throw new IllegalArgumentException("No valid enchantments found for AutoEnchantCrafting ability, ID: '" + getUniqueIdentifier() + "'");
	}

	public AutoEnchantCrafting(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.AUTO_ENCHANT_CRAFTING.get(), skill, data);

		CompoundTag enchantMap = data.getCompound("enchantments");
		enchantments = new ObjectIntPair[enchantMap.size()];
		int i = 0;

		for (String enchantId : enchantMap.getAllKeys()) {
			enchantments[i] = ObjectIntPair.of(EnchantmentUtil.toHolder(getPlayer().level(), ResourceLocation.read(enchantId).getOrThrow()), enchantMap.getInt(enchantId));
			i++;
		}
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	@Override
	protected void updateDescription(MutableComponent defaultDescription) {
		boolean comma = false;

		for (ObjectIntPair<Holder<Enchantment>> enchant : enchantments) {
			if (comma)
				defaultDescription.append(", ");

			defaultDescription.append(EnchantmentUtil.getFormattedName(enchant.left(), enchant.rightInt()));

			comma = true;
		}

		super.updateDescription(defaultDescription);
	}

	private void handleItemCrafting(ItemCraftingEvent ev) {
		if (ev.getCraftingInputs() instanceof ImbuingChamberMenu.ImbuingInventory)
			return;

		ItemStack output = ev.getOutputStack();

		if (output.is(Items.BOOK) || output.is(Items.ENCHANTED_BOOK))
			return;

		for (ObjectIntPair<Holder<Enchantment>> data : enchantments) {
			if (!output.isPrimaryItemFor(data.left()))
				return;
		}

		for (ObjectIntPair<Holder<Enchantment>> data : enchantments) {
			output.enchant(data.left(), data.rightInt());
		}
	}

	@Override
	public CompoundTag getSyncData(boolean forClientSetup) {
		CompoundTag data = super.getSyncData(forClientSetup);

		if (forClientSetup) {
			CompoundTag enchantMap = new CompoundTag();

			for (ObjectIntPair<Holder<Enchantment>> enchant : enchantments) {
				enchantMap.putInt(enchant.left().getRegisteredName(), enchant.rightInt());
			}

			data.put("enchantments", enchantMap);
		}

		return data;
	}
}
