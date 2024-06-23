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

public class BoreicBow extends BaseBow {
	public BoreicBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void tickArrow(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack) {
		if (arrow.isInWater()) {
			if (!arrow.level().isClientSide)
				WorldUtil.createExplosion(shooter, arrow.level(), arrow, arrow.isCritArrow() ? 2.0f : 1.0f);

			arrow.discard();
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
