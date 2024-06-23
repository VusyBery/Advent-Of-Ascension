package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class FaceMask extends AdventArmour {
	public FaceMask() {
		super(AoAArmourMaterials.FACE_MASK, ArmorItem.Type.HELMET, 36);
	}

	@Override
	public Type getSetType() {
		return Type.ALL;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARMOUR_AIRTIGHT, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
		tooltip.add(anySetEffectHeader());
	}
}
