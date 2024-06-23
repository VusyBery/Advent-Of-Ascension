package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class HydrangicArmour extends AdventArmour {
	public HydrangicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.HYDRANGIC, slot, 54);
	}

	@Override
	public Type getSetType() {
		return Type.HYDRANGIC;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.hydrangic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
