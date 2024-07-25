package net.tslat.aoa3.content.item.weapon.gun;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.GunRecoilPacket;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.content.entity.projectile.gun.LimoniteBulletEntity;
import net.tslat.aoa3.content.item.datacomponent.GunStats;
import net.tslat.aoa3.content.item.weapon.sniper.BaseSniper;
import net.tslat.aoa3.content.item.weapon.staff.BaseStaff;
import net.tslat.aoa3.library.builder.SoundBuilder;
import net.tslat.aoa3.util.*;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class BaseGun extends Item {
	public static final UUID BRACE_DEBUFF = UUID.fromString("a1371c64-c09e-4ed6-adfd-5afbaea79369");
	public static final UUID ATTACK_SPEED_MAINHAND = UUID.fromString("99fdc256-279e-4c8e-b1c6-9209571f134e");

	public BaseGun(Item.Properties properties) {
		super(properties);
	}

	public GunStats getGunStats() {
		return getGunStats(getDefaultInstance());
	}

	public GunStats getGunStats(ItemStack stack) {
		final GunStats stats = stack.get(AoADataComponents.GUN_STATS);

		return stats != null ? stats : getGunStats();
	}

	public float getGunDamage(ItemStack stack) {
		return getGunStats(stack).damage();
	}

	public float getRecoilModifier(ItemStack stack) {
		return getGunStats(stack).recoilModifier();
	}

	public int getTicksBetweenShots(ItemStack stack) {
		return getGunStats(stack).ticksBetweenShots();
	}

	private double getUnholsterTimeModifier(ItemStack stack) {
		return getGunStats(stack).unholsterTimeModifier();
	}

	@Nullable
	public SoundEvent getFiringSound() {
		return null;
	}

	protected float getFiringSoundPitchAdjust() {
		return 1f;
	}

	public float getRecoilForShot(ItemStack stack, LivingEntity shooter) {
		return (getGunDamage(stack) == 0 ? 1 : (float)Math.pow(getGunDamage(stack), 1.4f)) * getRecoilModifier(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}

	public Item getAmmoItem() {
		return AoAItems.LIMONITE_BULLET.get();
	}

	public InteractionHand getGunHand(Level level, ItemStack stack) {
		return EnchantmentUtil.hasEnchantment(level, stack, AoAEnchantments.BRACE) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairMaterial) {
		return false;
	}

	public boolean isFullAutomatic() {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (hand != getGunHand(level, stack))
			return InteractionResultHolder.fail(stack);

		if (player.isUsingItem() && player.isBlocking())
			return InteractionResultHolder.pass(stack);

		if (player.getAttackStrengthScale(0.0f) < 1)
			return InteractionResultHolder.fail(stack);

		if (hand == InteractionHand.OFF_HAND && player.isShiftKeyDown()) {
			Item mainItem = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();

			if (mainItem instanceof BaseSniper || mainItem instanceof BaseStaff)
				return InteractionResultHolder.fail(stack);
		}

		player.startUsingItem(hand);

		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity shooter, ItemStack stack, int count) {
		if (!isFullAutomatic() && count < getUseDuration(stack, shooter))
			return;

		if (level instanceof ServerLevel serverLevel) {
			ServerPlayer player = shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null;

			if (player == null || player.getCooldowns().getCooldownPercent(this, 0) == 0) {
				InteractionHand hand = getGunHand(level, stack);

				if (fireGun(serverLevel, shooter, stack, hand)) {
					ItemStack offhand;

					if (hand == InteractionHand.MAIN_HAND && EnchantmentUtil.hasEnchantment(level, (offhand = shooter.getOffhandItem()), AoAEnchantments.BRACE))
						offhand.onUseTick(serverLevel, shooter, count);

					ItemUtil.damageItemForUser(serverLevel, stack, shooter, hand == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);

					if (player != null) {
						player.awardStat(Stats.ITEM_USED.get(this));

						if (getTicksBetweenShots(stack) > 1)
							player.getCooldowns().addCooldown(this, getTicksBetweenShots(stack));

						doRecoil(player, stack, hand);
					}
				}
				else {
					shooter.releaseUsingItem();
				}
			}
		}
	}

	protected boolean fireGun(ServerLevel level, LivingEntity shooter, ItemStack stack, InteractionHand hand) {
		BaseBullet bullet = findAndConsumeAmmo(shooter, stack, hand);

		if (bullet == null)
			return false;

		shooter.level().addFreshEntity(bullet);
		doFiringEffects(level, shooter, bullet, stack, hand);

		return true;
	}

	public void doRecoil(ServerPlayer player, ItemStack stack, InteractionHand hand) {
		float recoilAmount = AoAEnchantments.modifyRecoil(player.serverLevel(), stack, getRecoilForShot(stack, player) * 2);

		AoANetworking.sendToPlayer(player, new GunRecoilPacket(hand == InteractionHand.OFF_HAND ? recoilAmount * 1.25f : recoilAmount));
	}

	protected void doFiringEffects(ServerLevel level, LivingEntity shooter, BaseBullet bullet, ItemStack stack, InteractionHand hand) {
		doFiringSound(shooter, bullet, stack, hand);

		level.sendParticles(ParticleTypes.SMOKE, bullet.getX(), bullet.getY(), bullet.getZ(), 2, 0, 0, 0, 0.025f);

		float gunDamage = getGunDamage(stack);

		if (gunDamage > 15) {
			if (gunDamage > 20)
				level.sendParticles(ParticleTypes.FLAME, bullet.getX(), bullet.getY(), bullet.getZ(), 2, 0, 0, 0, 0.025f);

			level.sendParticles(ParticleTypes.POOF, bullet.getX(), bullet.getY(), bullet.getZ(), 2, 0, 0, 0, 0.025f);
		}
	}

	public void doImpactDamage(Entity target, LivingEntity shooter, BaseBullet bullet, Vec3 impactPosition, float bulletDmgMultiplier) {
		if (target != null && target.level() instanceof ServerLevel level) {
			ItemStack stack = shooter.getItemInHand(bullet.getHand());
			float damage = getGunDamage(stack) * bulletDmgMultiplier;

			if (!stack.is(this))
				stack = getDefaultInstance();

			final ItemStack gunStack = stack;

			if (RandomUtil.percentChance(getTicksBetweenShots(stack) / 10f)) {
				if (DamageUtil.doHeavyGunAttack(shooter, bullet, target, source -> EnchantmentHelper.modifyDamage(level, gunStack, target, source, damage)))
					doImpactEffect(target, shooter, bullet, impactPosition, bulletDmgMultiplier);
			}
			else {
				if (DamageUtil.doGunAttack(shooter, bullet, target, source -> EnchantmentHelper.modifyDamage(level, gunStack, target, source, damage)))
					doImpactEffect(target, shooter, bullet, impactPosition, bulletDmgMultiplier);
			}
		}
	}

	protected void doImpactEffect(Entity target, LivingEntity shooter, BaseBullet bullet, Vec3 impactPos, float bulletDmgMultiplier) {}

	protected void doFiringSound(LivingEntity shooter, BaseBullet bullet, ItemStack stack, InteractionHand hand) {
		if (getFiringSound() != null)
			new SoundBuilder(getFiringSound()).isPlayer().pitch(getFiringSoundPitchAdjust()).varyPitch(0.075f).followEntity(shooter).execute();
	}

	@Nullable
	public BaseBullet findAndConsumeAmmo(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		if (!(shooter instanceof Player pl) || InventoryUtil.findItemForConsumption(pl, getAmmoItem(), pl.getAbilities().instabuild ? 0 : AoAEnchantments.modifyAmmoCost(pl.level(), gunStack, 1), true))
			return createProjectileEntity(shooter, gunStack, hand);

		return null;
	}

	public BaseBullet createProjectileEntity(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		return new LimoniteBulletEntity(shooter, this, hand, 120, 0);
	}

	public static ItemAttributeModifiers createGunAttributeModifiers(double holsterSpeed) {
		final ImmutableList.Builder<ItemAttributeModifiers.Entry> entries = ImmutableList.builder();

		entries.add(new ItemAttributeModifiers.Entry(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_ID, -holsterSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
				EquipmentSlotGroup.MAINHAND));
		entries.add(new ItemAttributeModifiers.Entry(
				Attributes.MOVEMENT_SPEED,
				new AttributeModifier(AdventOfAscension.id("brace_debuff"), -0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
				EquipmentSlotGroup.OFFHAND));

		return new ItemAttributeModifiers(entries.build(), false);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		float damage = getGunDamage(stack);

		if (damage > 0)
			tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.GUN_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(NumberUtil.roundToNthDecimalPlace(damage, 2))));

		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.FIRING_SPEED, LocaleUtil.ItemDescriptionType.NEUTRAL, Component.literal(NumberUtil.roundToNthDecimalPlace(20 / (float) getTicksBetweenShots(stack), 2))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.AMMO_ITEM, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, getAmmoItem().getDescription()));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(isFullAutomatic() ? LocaleUtil.Keys.FULLY_AUTOMATIC_GUN : LocaleUtil.Keys.SEMI_AUTOMATIC_GUN, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
	}
}
