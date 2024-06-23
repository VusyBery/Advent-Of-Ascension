package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class NightmareArmour extends AdventArmour {
	public NightmareArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.NIGHTMARE, slot, 63);
	}

	@Override
	public Type getSetType() {
		return Type.NIGHTMARE;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nightmare_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nightmare_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
