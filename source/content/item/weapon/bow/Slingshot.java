package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.content.entity.projectile.arrow.PopShotEntity;
import net.tslat.aoa3.util.EnchantmentUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Slingshot extends BaseBow {
	public static final Predicate<ItemStack> AMMO_PREDICATE = stack -> stack.getItem() == AoAItems.POP_SHOT.get() || stack.getItem() == Items.FLINT;

	public Slingshot(Item.Properties properties) {
		super(properties);
	}

	@Override
	public void onEntityImpact(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float velocity) {
		if (arrow instanceof PopShotEntity popShot && popShot.isExplosive)
			WorldUtil.createExplosion(shooter, arrow.level(), arrow, 1);

		arrow.discard();
	}

	@Override
	public void onBlockImpact(CustomArrowEntity arrow, @Nullable Entity shooter, BlockHitResult hitResult, ItemStack stack) {
		if (arrow instanceof PopShotEntity popShot && popShot.isExplosive)
			WorldUtil.createExplosion(shooter, arrow.level(), arrow, 1);

		arrow.discard();
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return AMMO_PREDICATE;
	}

	@Override
	public float getArrowDamage(CustomArrowEntity arrow, @Nullable Entity shooter, EntityHitResult hitResult, ItemStack stack, float baseDamage, float velocity, boolean isCritical) {
		float damage = baseDamage * (velocity / 2f);

		if (isCritical)
			damage += damage + (damage * RandomUtil.randomScaledGaussianValue(0.35f));

		return damage;
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		if (enchantment.is(Enchantments.PUNCH) || enchantment.is(Enchantments.FLAME))
			return false;

		return super.isPrimaryItemFor(stack, enchantment);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return super.isBookEnchantable(stack, book);
	}

	@Override
	protected CustomArrowEntity makeArrow(LivingEntity shooter, ItemStack bowStack, ItemStack ammoStack, float velocity, boolean consumeAmmo) {
		PopShotEntity popShot = new PopShotEntity(shooter.level(), bowStack, shooter, getBowDamage(bowStack), ammoStack.getItem() instanceof ArrowItem);
		int powerEnchant = EnchantmentUtil.getEnchantmentLevel(shooter.level(), bowStack, Enchantments.POWER);

		popShot.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0, velocity * 2, 1);

		if (powerEnchant > 0)
			popShot.setBaseDamage(popShot.getBaseDamage() + powerEnchant * 1.5D + 1D);

		return popShot;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARROW_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(Double.toString(getBowDamage(stack)))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.BOW_DRAW_TIME, LocaleUtil.ItemDescriptionType.NEUTRAL, Component.literal(Double.toString(((72000 / drawSpeedMultiplier) / 720) / (double)100))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.AMMO_ITEM, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, LocaleUtil.getLocaleMessage(AoAItems.POP_SHOT.get().getDescriptionId()).append(Component.literal("/")).append(LocaleUtil.getLocaleMessage(Items.FLINT.getDescriptionId()))));
	}
}
