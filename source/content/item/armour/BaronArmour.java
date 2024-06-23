package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class BaronArmour extends AdventArmour {
	public BaronArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.BARON, slot, 150);
	}

	@Override
	public Type getSetType() {
		return Type.BARON;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.baron_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
