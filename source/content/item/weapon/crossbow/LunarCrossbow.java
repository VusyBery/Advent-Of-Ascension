package net.tslat.aoa3.content.item.weapon.crossbow;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class LunarCrossbow extends BaseCrossbow {
	public LunarCrossbow(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack stack, ItemStack ammoStack, boolean isCrit) {
		Projectile projectile = super.createProjectile(level, shooter, stack, ammoStack, isCrit);

		projectile.setNoGravity(true);

		return projectile;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
	}
}
