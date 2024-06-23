package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UtopianArmour extends AdventArmour {
	public UtopianArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.UTOPIAN, slot, new Item.Properties().durability(slot.getDurability(50)).rarity(Rarity.RARE));
	}

	@Override
	public Type getSetType() {
		return Type.UTOPIAN;
	}

	@Override
	public void onEquip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		if (slot == null) {
			plData.getSkills().forEach(skill -> skill.applyXpModifier(0.1f));
		}
		else {
			plData.getSkills().forEach(skill -> skill.applyXpModifier(0.05f));
		}
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		if (slot == null) {
			plData.getSkills().forEach(skill -> skill.removeXpModifier(0.1f));
		}
		else {
			plData.getSkills().forEach(skill -> skill.removeXpModifier(0.05f));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.utopian_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.utopian_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
