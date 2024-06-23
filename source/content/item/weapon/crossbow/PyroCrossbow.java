package net.tslat.aoa3.content.item.weapon.crossbow;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PyroCrossbow extends BaseCrossbow {
	public PyroCrossbow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public CustomArrowEntity applyArrowMods(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack, boolean isCritical) {
		arrow.igniteForSeconds(100);

		return arrow;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.BURNS_TARGETS, LocaleUtil.ItemDescriptionType.BENEFICIAL));
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
	}
}
