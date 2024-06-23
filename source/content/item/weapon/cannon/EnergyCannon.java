package net.tslat.aoa3.content.item.weapon.cannon;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.content.entity.projectile.cannon.EnergyShotEntity;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyCannon extends BaseCannon {
	public EnergyCannon(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_ENERGY_CANNON_FIRE.get();
	}

	@Override
	public BaseBullet createProjectileEntity(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		return new EnergyShotEntity(shooter, this, hand, 120, 0);
	}

	@Override
	public void doImpactDamage(Entity target, LivingEntity shooter, BaseBullet bullet, Vec3 impactPosition, float bulletDmgMultiplier) {
		if (target != null) {
			if (target instanceof LivingEntity)
				bulletDmgMultiplier *= 1 + (((LivingEntity)target).getAttribute(Attributes.ARMOR).getValue() * 1.50) / 100;

			ItemStack stack = shooter.getItemInHand(bullet.getHand());

			if (!stack.is(this))
				stack = getDefaultInstance();

			float damage = getGunDamage(stack) * bulletDmgMultiplier * 0.75f;

			if (DamageUtil.doHeavyGunAttack(shooter, bullet, target, source -> damage)) {
				if (target instanceof Player pl && pl.isBlocking())
					pl.disableShield();

				if (target instanceof LivingEntity livingTarget)
					DamageUtil.doScaledKnockback(livingTarget, shooter, damage / 20f, 1, 1, 1);
			}

			DamageUtil.doEnergyProjectileAttack(shooter, bullet, target, damage / 3f);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
