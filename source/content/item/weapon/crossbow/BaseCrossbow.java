package net.tslat.aoa3.content.item.weapon.crossbow;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.content.item.ArrowFiringWeapon;
import net.tslat.aoa3.content.item.datacomponent.CrossbowStats;
import net.tslat.aoa3.util.InteractionResults;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class BaseCrossbow extends CrossbowItem implements ArrowFiringWeapon {
	protected double damage;

	public BaseCrossbow(Item.Properties properties) {
		super(properties.component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY));
	}

	public CrossbowStats crossbowStats() {
		return crossbowStats(getDefaultInstance());
	}

	public CrossbowStats crossbowStats(ItemStack stack) {
		return stack.get(AoADataComponents.CROSSBOW_STATS.get());
	}

	public double getCrossbowDamage(ItemStack stack) {
		return crossbowStats(stack).damage();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.CROSSBOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ChargedProjectiles loadedProjectiles = stack.get(DataComponents.CHARGED_PROJECTILES);

		if (loadedProjectiles != null && !loadedProjectiles.isEmpty()) {
			performShooting(level, player, hand, stack, getShootingPower(loadedProjectiles), 1, null);

			return InteractionResults.ItemUse.succeedNoArmSwing(stack);
		}

		if (getAmmoStack(player, stack).isEmpty())
			return InteractionResults.ItemUse.denyUsage(stack);

		this.startSoundPlayed = false;
		this.midLoadSoundPlayed = false;
		player.startUsingItem(hand);

		return InteractionResults.ItemUse.succeedNoArmSwing(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int ticksRemaining) {
		int ticksCharged = getUseDuration(stack, shooter) - ticksRemaining;
		float chargePower = getPowerForTime(ticksCharged, stack, shooter);

		if (chargePower >= 1 && !isCharged(stack) && tryLoadProjectiles(shooter, stack))
			level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_LOADING_END, shooter.getSoundSource(), 1, 1 / (level.getRandom().nextFloat() * 0.5f + 1) + 0.2f);
	}

	protected boolean tryLoadProjectiles(LivingEntity shooter, ItemStack stack) {
		final List<ItemStack> ammoStacks = draw(stack, getAmmoStack(shooter, stack), shooter);

		if (ammoStacks.isEmpty())
			return false;

		stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(ammoStacks));

		return true;
	}

	protected float getPowerForTime(int useTicks, ItemStack pCrossbowStack, LivingEntity shooter) {
		return Math.min(1, (float)useTicks / (float)getChargeDuration(pCrossbowStack, shooter));
	}

	protected ItemStack getAmmoStack(LivingEntity shooter, ItemStack crossbowStack) {
		return shooter.getProjectile(crossbowStack);
	}

	@Override
	protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack stack, ItemStack ammoStack, boolean isCrit) {
		Projectile projectile = super.createProjectile(level, shooter, stack, ammoStack, isCrit);

		if (!(projectile instanceof AbstractArrow arrow))
			return projectile;

		return applyArrowMods(CustomArrowEntity.fromArrow(arrow, stack, shooter, getCrossbowDamage(stack)), shooter, stack, isCrit);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARROW_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(Double.toString(getCrossbowDamage(stack)))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.AMMO_ITEM, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, Component.translatable(Items.ARROW.getDescriptionId())));
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
	}
}
