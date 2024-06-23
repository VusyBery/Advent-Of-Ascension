package net.tslat.aoa3.content.item.weapon.vulcane;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class EqualityVulcane extends BaseVulcane {
	public EqualityVulcane(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void doAdditionalEffect(LivingEntity target, Player attacker, float damageDealt) {
		EntityUtil.healEntity(attacker, damageDealt / 2f);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
