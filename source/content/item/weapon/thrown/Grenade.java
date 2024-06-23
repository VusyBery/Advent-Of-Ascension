package net.tslat.aoa3.content.item.weapon.thrown;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoAExplosions;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.content.entity.projectile.thrown.GrenadeEntity;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class Grenade extends BaseThrownWeapon {
	public Grenade(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected float getFiringSoundPitchAdjust() {
		return 0.5f;
	}

	@Override
	public BaseBullet createProjectileEntity(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		return new GrenadeEntity(shooter, this);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);

		for (MutableComponent component : LocaleUtil.getExplosionInfoLocale(AoAExplosions.GRENADE, flag.isAdvanced(), true)) {
			tooltip.add(2, component);
		}
	}

	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
		return new GrenadeEntity(level, position.x(), position.y(), position.z());
	}
}
