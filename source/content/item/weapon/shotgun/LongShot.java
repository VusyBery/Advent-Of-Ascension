package net.tslat.aoa3.content.item.weapon.shotgun;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.content.entity.projectile.gun.LimoniteBulletEntity;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.List;

public class LongShot extends BaseShotgun {
	public LongShot(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean fireGun(ServerLevel level, LivingEntity shooter, ItemStack stack, InteractionHand hand) {
		BaseBullet bullet = findAndConsumeAmmo(shooter, stack, hand);

		if (bullet == null)
			return false;

		int pellets = getPelletCount(stack);
		float spreadFactor = getSpreadFactor(shooter, stack, pellets);

		for (int i = 0; i < pellets; i++) {
			BaseBullet pellet = new LimoniteBulletEntity(shooter, this, hand, 6, 1.0f, 0, (float)RandomUtil.randomValueBetween(-0.5f, 0.5f) * spreadFactor, (float)RandomUtil.randomValueBetween(-0.5f, 0.5f) * spreadFactor, (float)RandomUtil.randomValueBetween(-0.5f, 0.5f) * spreadFactor);

			level.addFreshEntity(pellet);
		}

		doFiringEffects(level, shooter, bullet, stack, hand);

		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
