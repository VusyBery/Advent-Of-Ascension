package net.tslat.aoa3.content.item.weapon.thrown;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.content.item.weapon.gun.BaseGun;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseThrownWeapon extends BaseGun implements ProjectileItem {
	public BaseThrownWeapon(Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isFullAutomatic() {
		return false;
	}

	@Override
	public Item getAmmoItem() {
		return this;
	}

	@Override
	public float getRecoilForShot(ItemStack stack, LivingEntity shooter) {
		return 0;
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return SoundEvents.SPLASH_POTION_THROW;
	}

	@Override
	public void doImpactDamage(Entity target, LivingEntity shooter, BaseBullet bullet, Vec3 impactPosition, float bulletDmgMultiplier) {}

	@Override
	public BaseBullet findAndConsumeAmmo(LivingEntity shooter, ItemStack weaponStack, InteractionHand hand) {
		if (weaponStack.isEmpty())
			return null;

		if (!shooter.level().isClientSide() && shooter instanceof Player && !((Player)shooter).isCreative())
			weaponStack.shrink(1);

		return createProjectileEntity(shooter, weaponStack, hand);
	}

	@Override
	protected void doFiringEffects(ServerLevel level, LivingEntity shooter, BaseBullet bullet, ItemStack stack, InteractionHand hand) {
		doFiringSound(shooter, bullet, stack, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (getGunDamage(stack) > 0.0f)
			tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.RANGED_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, LocaleUtil.numToComponent(getGunDamage(stack))));

		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.THROWN_WEAPON, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.THROWN_WEAPON_RATE, LocaleUtil.ItemDescriptionType.NEUTRAL, Component.literal(NumberUtil.roundToNthDecimalPlace(20 / (float)getTicksBetweenShots(stack), 2))));
	}
}
