package net.tslat.aoa3.content.item.food;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoAFood;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.*;

import java.util.List;

public class HeartFruit extends Item {
	public HeartFruit() {
		super(new Item.Properties().food(AoAFood.HEART_FRUIT));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide) {
			DamageUtil.doRecoilAttack(entity, 7);

			if (entity.getHealth() > 0 && WorldUtil.isWorld(level, AoADimensions.PRECASIA) && entity instanceof ServerPlayer pl && InventoryUtil.findItemForConsumption(pl, AoAItems.BLANK_REALMSTONE, 1, true))
				InventoryUtil.giveItemTo(pl, AoAItems.CANDYLAND_REALMSTONE);
		}

		return super.finishUsingItem(stack, level, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.NEUTRAL, 1));
	}
}