package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.common.registration.AoAExplosions;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.library.object.explosion.StandardExplosion;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosiveBow extends BaseBow {
	public ExplosiveBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {
		if (arrow.isCritArrow() && arrow.level() instanceof ServerLevel serverLevel)
			new StandardExplosion(AoAExplosions.EXPLOSIVE_BOW, serverLevel, arrow, shooter).explode();
	}

	@Override
	public void onBlockImpact(CustomArrowEntity arrow, @Nullable Entity shooter, BlockHitResult hitResult, ItemStack stack) {
		if (arrow.isCritArrow() && arrow.level() instanceof ServerLevel serverLevel)
			new StandardExplosion(AoAExplosions.EXPLOSIVE_BOW, serverLevel, arrow, shooter).explode();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);

		for (MutableComponent component : LocaleUtil.getExplosionInfoLocale(AoAExplosions.EXPLOSIVE_BOW, flag.isAdvanced(), false)) {
			tooltip.add(2, component);
		}
	}
}
