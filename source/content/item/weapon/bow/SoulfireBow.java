package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulfireBow extends BaseBow {
	public SoulfireBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected CustomArrowEntity makeArrow(LivingEntity shooter, ItemStack bowStack, ItemStack ammoStack, float velocity, boolean consumeAmmo) {
		CustomArrowEntity arrow = super.makeArrow(shooter, bowStack, ammoStack, velocity, consumeAmmo);

		if (arrow != null && shooter instanceof ServerPlayer pl && PlayerUtil.consumeResource(pl, AoAResources.SPIRIT.get(), 200, false))
			arrow.setGlowingTag(true);

		return arrow;
	}

	@Override
	public void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {
		if (arrow.isCurrentlyGlowing() && shooter instanceof LivingEntity livingShooter)
			EntityUtil.healEntity(livingShooter, 8);

		arrow.setGlowingTag(false);
	}

	@Override
	public void onBlockImpact(CustomArrowEntity arrow, @Nullable Entity shooter, BlockHitResult hitResult, ItemStack stack) {
		arrow.setGlowingTag(false);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 2));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
