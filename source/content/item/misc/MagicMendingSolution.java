package net.tslat.aoa3.content.item.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.item.ChargeableItem;
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class MagicMendingSolution extends Item implements ChargeableItem {
	public MagicMendingSolution() {
		super(new Item.Properties().stacksTo(1).craftRemainder(AoAItems.METAL_TUB.get()).component(AoADataComponents.CHARGE, 600f));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
		subtractCharge(stack, getCharge(stack), true);

		if (getCharge(stack) <= 0 && !level.isClientSide) {
			stack.shrink(1);

			if (entity instanceof Player pl)
				ItemUtil.givePlayerMultipleItems(pl, AoAItems.METAL_TUB.toStack(), AoAItems.MAGIC_MENDING_COMPOUND.toStack());
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.NEUTRAL, 1));
	}
}
