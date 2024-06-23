package net.tslat.aoa3.content.item.weapon.sword;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class RockbasherSword extends BaseSword {
	public RockbasherSword(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	protected void doMeleeEffect(ItemStack stack, LivingEntity target, LivingEntity attacker, float attackCooldown) {
		double armour = target.getAttribute(Attributes.ARMOR).getValue();

		if (armour > 0)
			WorldUtil.createExplosion(attacker, attacker.level(), target.getX(), target.getY() + target.getBbHeight() / 1.5, target.getZ(), 0.5f + (float)(3 * armour / 30f));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}
}
