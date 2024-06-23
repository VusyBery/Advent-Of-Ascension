package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HauntedBow extends BaseBow {
	public HauntedBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public CustomArrowEntity applyArrowMods(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack, boolean isCritical) {
		arrow.setIgnoreExplosions();

		return arrow;
	}

	@Override
	public void tickArrow(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack) {
		if (!arrow.level().isClientSide && !arrow.inGround && arrow.tickCount % 2 == 0)
			WorldUtil.createExplosion(shooter, arrow.level(), arrow, 1);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
