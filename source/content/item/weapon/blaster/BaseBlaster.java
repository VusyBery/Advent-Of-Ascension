package net.tslat.aoa3.content.item.weapon.blaster;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.AoASoundBuilderPacket;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.item.EnergyProjectileWeapon;
import net.tslat.aoa3.content.item.armour.AdventArmour;
import net.tslat.aoa3.content.item.datacomponent.BlasterStats;
import net.tslat.aoa3.library.builder.SoundBuilder;
import net.tslat.aoa3.library.constant.AttackSpeed;
import net.tslat.aoa3.util.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseBlaster extends Item implements EnergyProjectileWeapon {
	public BaseBlaster(Item.Properties properties) {
		super(properties);
	}

	public BlasterStats blasterStats() {
		return blasterStats(getDefaultInstance());
	}

	public BlasterStats blasterStats(ItemStack stack) {
		return stack.get(AoADataComponents.BLASTER_STATS.get());
	}

	public float getBlasterDamage(ItemStack stack) {
		return blasterStats(stack).damage();
	}

	public int getTicksBetweenShots(ItemStack stack) {
		return blasterStats(stack).ticksBetweenShots();
	}

	public int getChargeTime(ItemStack stack) {
		return blasterStats(stack).chargeUpTicks();
	}

	public float getBaseSpiritCost(ItemStack stack) {
		return blasterStats(stack).energyCost();
	}

	@Nullable
	public SoundEvent getFiringSound() {
		return null;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairMaterial) {
		return false;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.NONE;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (hand != getWeaponHand(player))
			return InteractionResults.ItemUse.denyUsage(stack);

		if (player.getAttackStrengthScale(0.0f) < 1)
			return InteractionResults.ItemUse.denyUsage(stack);

		final float energyCost = getSpiritCost(stack, player, false);

		if (player.getAbilities().instabuild || PlayerUtil.getResourceValue(player, AoAResources.SPIRIT.get()) >= energyCost) {
			player.startUsingItem(hand);

			return InteractionResults.ItemUse.noActionTaken(stack);
		}
		else if (!player.getAbilities().instabuild) {
			return InteractionResults.ItemUse.denyUsage(stack);
		}

		return InteractionResults.ItemUse.noActionTaken(stack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity shooter, ItemStack stack, int count) {
		if (getUseDuration(stack, shooter) - count < getChargeTime(stack) - 2)
			return;

		if (level instanceof ServerLevel serverLevel) {
			ServerPlayer player = shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null;

			if (player == null || player.getCooldowns().getCooldownPercent(this, 0) == 0) {
				if (tryFireBlaster(serverLevel, shooter, stack, player)) {
					ItemUtil.damageItem(stack, shooter, shooter.getUsedItemHand(), 1);

					if (player != null) {
						player.awardStat(Stats.ITEM_USED.get(this));

						if (getTicksBetweenShots(stack) > 1)
							player.getCooldowns().addCooldown(this, getTicksBetweenShots(stack));
					}
				}
				else {
					shooter.releaseUsingItem();
				}
			}
		}
	}

	protected boolean tryFireBlaster(ServerLevel level, LivingEntity shooter, ItemStack stack, @Nullable ServerPlayer playerShooter) {
		final float energyCost = getSpiritCost(stack, shooter, false);

		if (energyCost == 0 || consumeEnergy(playerShooter, stack, energyCost)) {
			ShotInfo shotInfo = fireBlaster(level, shooter, stack, true);

			if (shotInfo != null)
				doFiringEffects(level, shooter, stack, shotInfo);

			if (getFiringSound() != null)
				AoANetworking.sendToAllPlayersTrackingEntity(new AoASoundBuilderPacket(new SoundBuilder(getFiringSound()).isPlayer().followEntity(shooter)), shooter);

			return true;
		}
		else {
			PlayerUtil.notifyPlayerOfInsufficientResources(playerShooter, AoAResources.SPIRIT.get(), energyCost);
		}

		return false;
	}

	protected ShotInfo fireBlaster(ServerLevel level, LivingEntity shooter, ItemStack blaster, boolean isOldBlaster) {
		fireBlaster(level, shooter, blaster);

		if (isOldBlaster)
			return null;

		final float beamDistance = getBeamDistance(blaster, shooter);
		final ShotInfo shotInfo = getPosAndRotForShot(this, shooter, 1f, beamDistance);
		Vec3 endPos = shotInfo.position().add(shotInfo.angle().scale(beamDistance));
		final HitResult rayTrace = PositionAndMotionUtil.rayTrace(shooter.level(), shotInfo.position(), endPos, ClipContext.Block.OUTLINE, true, true, entity -> entity != shooter && entity instanceof LivingEntity);

		shotInfo.setHitPos(rayTrace.getLocation());

		if (rayTrace.getType() != HitResult.Type.MISS) {
			switch (rayTrace) {
				case EntityHitResult entityHit -> shotInfo.setEffectiveHit(doEntityImpact(level, shooter, blaster, shotInfo, entityHit));
				case BlockHitResult blockHit -> shotInfo.setEffectiveHit(doBlockImpact(level, shooter, blaster, shotInfo, blockHit));
				default -> {}
			}
		}

		return shotInfo;
	}

	protected void fireBlaster(ServerLevel level, LivingEntity shooter, ItemStack blaster) {}

	protected void doFiringEffects(ServerLevel level, LivingEntity shooter, ItemStack stack, ShotInfo shotInfo) {}

	@Override
	public int getBarWidth(ItemStack stack) {
		final float chargeProgress = getChargeProgress(stack);

		if (chargeProgress > 0)
			return Mth.floor(chargeProgress * 13);

		return super.getBarWidth(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return super.isBarVisible(stack) || getChargeProgress(stack) > 0;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		final float chargeProgress = getChargeProgress(stack);

		if (chargeProgress > 0)
			return 0x00C1DB;

		return super.getBarColor(stack);
	}

	private float getChargeProgress(ItemStack stack) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			Player player = ClientOperations.getPlayer();

			if (player.getUseItem() == stack) {
				final int useTime = stack.getUseDuration(player) - player.getUseItemRemainingTicks();
				final int chargeTime = getChargeTime(stack);

				if (useTime < chargeTime - 1)
					return useTime / (float)(chargeTime - 1);
			}
		}

		return 0;
	}

	public float getSpiritCost(ItemStack stack, @Nullable LivingEntity shooter, boolean forDisplay) {
		if (!(shooter instanceof Player pl) || (pl.getAbilities().instabuild && !forDisplay))
			return 0;

		float spiritCost = getBaseSpiritCost(stack);

		if (pl.level() instanceof ServerLevel level) {
			spiritCost = AoAEnchantments.modifySpiritConsumption(level, stack, spiritCost);
			spiritCost *= (1 + AoAEnchantments.modifyAmmoCost(level, stack, 0) * 0.3f);
		}

		if (PlayerUtil.isWearingFullSet(pl, AdventArmour.Type.GHOULISH))
			spiritCost *= 0.7f;

		return spiritCost;
	}

	public float getBeamDistance(ItemStack stack, @Nullable LivingEntity shooter) {
		return 30;
	}

	public boolean consumeEnergy(ServerPlayer player, ItemStack stack, float cost) {
		return PlayerUtil.consumeResource(player, AoAResources.SPIRIT.get(), cost, false);
	}

	@Override
	public InteractionHand getWeaponHand(LivingEntity holder) {
		return InteractionHand.MAIN_HAND;
	}

	@Override
	//@Deprecated(forRemoval = true)
	public void doBlockImpact(BaseEnergyShot shot, Vec3 hitPos, LivingEntity shooter) {}

	@Override
	//@Deprecated(forRemoval = true)
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {
		ItemStack stack = shooter.getItemInHand(InteractionHand.MAIN_HAND);

		if (!stack.is(this))
			stack = getDefaultInstance();

		if (DamageUtil.doEnergyProjectileAttack(shooter, shooter, target, getBlasterDamage(stack))) {
			doImpactEffect(shot, target, shooter);

			return true;
		}

		return false;
	}

	@Override
	public boolean doEntityImpact(ServerLevel level, LivingEntity shooter, ItemStack stack, ShotInfo shotInfo, EntityHitResult rayTrace) {
		if (DamageUtil.doEnergyProjectileAttack(shooter, shooter, rayTrace.getEntity(), getBlasterDamage(stack))) {
			doImpactEffect(level, shooter, stack, shotInfo, rayTrace, true);

			return true;
		}

		doImpactEffect(level, shooter, stack, shotInfo, rayTrace, false);

		return false;
	}

	@Override
	public boolean doBlockImpact(ServerLevel level, LivingEntity shooter, ItemStack stack, ShotInfo shotInfo, BlockHitResult rayTrace) {
		doImpactEffect(level, shooter, stack, shotInfo, rayTrace, true);

		return true;
	}

	protected void doImpactEffect(ServerLevel level, LivingEntity shooter, ItemStack stack, ShotInfo shotInfo, HitResult rayTrace, boolean affectedTarget) {}

	//@Deprecated(forRemoval = true)
	protected void doImpactEffect(BaseEnergyShot shot, Entity target, LivingEntity shooter) {}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}

	public static ItemAttributeModifiers createBlasterAttributeModifiers(float attacksPerSecond) {
		final ImmutableList.Builder<ItemAttributeModifiers.Entry> entries = ImmutableList.builder();

		entries.add(new ItemAttributeModifiers.Entry(
				Attributes.ATTACK_SPEED,
				new AttributeModifier(BASE_ATTACK_SPEED_ID, AttackSpeed.forAttacksPerSecond(attacksPerSecond), AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND));

		return new ItemAttributeModifiers(entries.build(), false);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (getBlasterDamage(stack) > 0)
			tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ENERGY_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(NumberUtil.roundToNthDecimalPlace(getBlasterDamage(stack), 1))));

		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.BLASTER_CHARGE, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO, Component.literal(NumberUtil.roundToNthDecimalPlace((float)getChargeTime(stack) / 20f, 2))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.BLASTER_PENETRATION, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.FIRING_SPEED, LocaleUtil.ItemDescriptionType.NEUTRAL, Component.literal(NumberUtil.roundToNthDecimalPlace(20 / (float) getTicksBetweenShots(stack), 2))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.AMMO_RESOURCE, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, Component.literal(NumberUtil.roundToNthDecimalPlace(getSpiritCost(stack, FMLEnvironment.dist == Dist.CLIENT ? ClientOperations.getPlayer() : null, true), 2)), AoAResources.SPIRIT.get().getName()));
	}
}
