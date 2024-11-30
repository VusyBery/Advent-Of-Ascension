package net.tslat.aoa3.player.ability.imbuing;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.event.custom.events.GrindstoneResultEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;

import java.util.List;

public class GrindstoneCursesRemoval extends AoAAbility.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(GrindstoneResultEvent.class, GrindstoneResultEvent::getEntity, serverOnly(this::handleGrindstoneModifying)));

	public GrindstoneCursesRemoval(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.GRINDSTONE_CURSES_REMOVAL.get(), skill, data);
	}

	public GrindstoneCursesRemoval(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.GRINDSTONE_CURSES_REMOVAL.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleGrindstoneModifying(final GrindstoneResultEvent ev) {
		ItemEnchantments remainingEnchants = EnchantmentHelper.updateEnchantments(ev.getOriginalOutput(), enchants -> enchants.removeIf(enchantmentIntegerEntry -> enchantmentIntegerEntry.is(EnchantmentTags.CURSE)));

		if (ev.getOutput().is(Items.ENCHANTED_BOOK) && remainingEnchants.isEmpty())
			ev.setNewOutput(ev.getOutput().transmuteCopy(Items.BOOK));
	}
}
