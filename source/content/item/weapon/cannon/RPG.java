package net.tslat.aoa3.content.item.weapon.cannon;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoAExplosions;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAWeapons;
import net.tslat.aoa3.content.entity.projectile.cannon.RPGEntity;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.util.AdvancementUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RPG extends BaseCannon {
	public RPG(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_RPG_FIRE.get();
	}

	@Override
	public Item getAmmoItem() {
		return AoAWeapons.GRENADE.get();
	}

	@Override
	public BaseBullet createProjectileEntity(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		return new RPGEntity(shooter, this, hand, 120, 0);
	}

	@Override
	public void doImpactDamage(Entity target, LivingEntity shooter, BaseBullet bullet, Vec3 impactPosition, float bulletDmgMultiplier) {
		if (target != null) {
			if (target instanceof LivingEntity livingTarget)
				bulletDmgMultiplier *= 1 + (livingTarget.getAttributeValue(Attributes.ARMOR) * 1.5 + livingTarget.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * 0.5f) / 100f;

			ItemStack stack = shooter.getItemInHand(bullet.getHand());

			if (!stack.is(this))
				stack = getDefaultInstance();

			float damage = getGunDamage(stack) * bulletDmgMultiplier;

			if (DamageUtil.doHeavyGunAttack(shooter, bullet, target, source -> damage) && shooter instanceof ServerPlayer pl) {
				if (target instanceof LivingEntity livingTarget && livingTarget.getHealth() == 0 && target.hasImpulse) {
					if (target.level().isEmptyBlock(target.blockPosition().below()) && target.level().isEmptyBlock(target.blockPosition().below(2)))
						AdvancementUtil.grantCriterion(pl, AdventOfAscension.id("completionist/surface_to_air"), "rpg_air_kill");
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);

		for (MutableComponent component : LocaleUtil.getExplosionInfoLocale(AoAExplosions.rpg(entity -> 1f), flag.isAdvanced(), false)) {
			tooltip.add(2, component);
		}
	}
}
