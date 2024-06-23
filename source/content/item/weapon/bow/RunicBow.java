package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RunicBow extends BaseBow {
	public RunicBow(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {
		if (hitResult.getEntity() instanceof LivingEntity target && arrow.isCritArrow() && shooter instanceof LivingEntity) {
			for (LivingEntity entity : EntityRetrievalUtil.<LivingEntity>getEntities(arrow, 3, entity -> entity instanceof Enemy && entity instanceof LivingEntity)) {
				DamageUtil.doMiscMagicAttack(shooter, entity, 2, target.position());
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}