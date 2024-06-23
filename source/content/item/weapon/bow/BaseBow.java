package net.tslat.aoa3.content.item.weapon.bow;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.content.item.ArrowFiringWeapon;
import net.tslat.aoa3.content.item.datacomponent.BowStats;
import net.tslat.aoa3.util.EnchantmentUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;

import java.util.List;

public class BaseBow extends BowItem implements ArrowFiringWeapon {
	protected float drawSpeedMultiplier;
	protected double dmg;

	public BaseBow(Item.Properties properties) {
		super(properties);
	}

	public BowStats bowStats() {
		return components().get(AoADataComponents.BOW_STATS.get());
	}

	public float getBowDamage() {
		return bowStats().damage();
	}

	public float getDrawSpeedMultiplier() {
		return bowStats().drawSpeedModifier();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);
		boolean hasAmmo = !findAmmo(player, heldStack, player.isCreative()).isEmpty();
		InteractionResultHolder<ItemStack> arrowNockEventResult = EventHooks.onArrowNock(heldStack, world, player, hand, hasAmmo);

		if (arrowNockEventResult != null)
			return arrowNockEventResult;

		if (!player.isCreative() && !hasAmmo) {
			return InteractionResultHolder.fail(heldStack);
		}
		else {
			player.startUsingItem(hand);
			return InteractionResultHolder.consume(heldStack);
		}
	}

	public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
		if (!(shooter instanceof Player))
			return;

		Player pl = (Player)shooter;
		boolean infiniteAmmo = pl.isCreative() || EnchantmentUtil.hasEnchantment(level, stack, Enchantments.INFINITY);
		ItemStack ammoStack = findAmmo(pl, stack, infiniteAmmo);
		int charge = (int)((getUseDuration(stack, shooter) - timeLeft) * getDrawSpeedMultiplier());
		charge = EventHooks.onArrowLoose(stack, level, pl, charge, !ammoStack.isEmpty() || infiniteAmmo);

		if (charge < 0)
			return;

		if (!ammoStack.isEmpty() || infiniteAmmo) {
			if (ammoStack.isEmpty())
				ammoStack = new ItemStack(Items.ARROW);

			float velocity = getPowerForTime(charge);

			if (velocity >= 0.1f) {
				if (!level.isClientSide) {
					CustomArrowEntity arrow = makeArrow(pl, stack, ammoStack, velocity, !infiniteAmmo);

					arrow = applyArrowMods(arrow, shooter, ammoStack, arrow.isCritArrow());
					level.addFreshEntity(arrow);
				}

				level.playSound(null, pl.getX(), pl.getY(), pl.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (float)RandomUtil.randomValueBetween(1.2f, 1.6f) + velocity * 0.5F);

				if (!infiniteAmmo && !pl.getAbilities().instabuild) {
					ammoStack.shrink(1);

					if (ammoStack.isEmpty())
						pl.getInventory().removeItem(ammoStack);
				}

				pl.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	protected ItemStack findAmmo(Player shooter, ItemStack bowStack, boolean infiniteAmmo) {
		return shooter.getProjectile(bowStack);
	}

	protected CustomArrowEntity makeArrow(LivingEntity shooter, ItemStack bowStack, ItemStack ammoStack, float velocity, boolean consumeAmmo) {
		ArrowItem arrowItem = (ArrowItem)(ammoStack.getItem() instanceof ArrowItem ? ammoStack.getItem() : Items.ARROW);
		CustomArrowEntity arrow = CustomArrowEntity.fromArrow(arrowItem.createArrow(shooter.level(), ammoStack, shooter, bowStack), bowStack, shooter, getBowDamage());

		arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity * 3.0F, 1.0F);

		if (velocity == 1)
			arrow.setCritArrow(true);

		bowStack.hurtAndBreak(getDurabilityUse(bowStack), shooter, EntityUtil.handToEquipmentSlotType(shooter.getUsedItemHand()));

		if (!consumeAmmo || (shooter instanceof Player pl && pl.isCreative()) && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW))
			arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

		return arrow;
	}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARROW_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(Float.toString(getBowDamage()))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.BOW_DRAW_TIME, LocaleUtil.ItemDescriptionType.NEUTRAL, Component.literal(Double.toString(((int)(72000 / getDrawSpeedMultiplier()) / 720) / 100d))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.AMMO_ITEM, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, LocaleUtil.getLocaleMessage(Items.ARROW.getDescriptionId())));
	}
}