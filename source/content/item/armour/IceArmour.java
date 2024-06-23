package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class IceArmour extends AdventArmour {
	public IceArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ICE, slot, 12);
	}

	@Override
	public Type getSetType() {
		return Type.ICE;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (!level.isClientSide && isInArmourSlot(slotId) && entity instanceof LivingEntity wearer) {
			if (stack.getDamageValue() > 0 && wearer.getRandom().nextFloat() < 0.02f && WorldUtil.getAmbientTemperature(level, wearer.blockPosition()) < 0.15f)
				stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.ice_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
