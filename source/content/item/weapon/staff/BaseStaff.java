package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.AoASoundBuilderPacket;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.item.EnergyProjectileWeapon;
import net.tslat.aoa3.content.item.datacomponent.StaffRuneCost;
import net.tslat.aoa3.content.item.weapon.blaster.BaseBlaster;
import net.tslat.aoa3.content.item.weapon.gun.BaseGun;
import net.tslat.aoa3.library.builder.SoundBuilder;
import net.tslat.aoa3.util.InteractionResults;
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class BaseStaff<T> extends Item implements EnergyProjectileWeapon {
	public BaseStaff(Item.Properties properties) {
		super(properties);
	}

	public StaffRuneCost runeCost() {
		return runeCost(getDefaultInstance());
	}

	public StaffRuneCost runeCost(ItemStack stack) {
		return stack.get(AoADataComponents.STAFF_RUNE_COST.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (hand == InteractionHand.OFF_HAND) {
			Item mainItem = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();

			if (mainItem instanceof BaseGun || mainItem instanceof BaseBlaster)
				return InteractionResultHolder.fail(stack);
		}

		if (player instanceof ServerPlayer pl) {
			return checkPreconditions(pl, stack).filter(data -> findAndConsumeRunes(runeCost(stack).runeCosts(), pl, true, stack)).map(data -> {
				if (getCastingSound() != null)
					AoANetworking.sendToAllPlayersTrackingEntity(new AoASoundBuilderPacket(new SoundBuilder(getCastingSound()).isPlayer().followEntity(pl)), pl);

				pl.getCooldowns().addCooldown(this, 24);
				pl.awardStat(Stats.ITEM_USED.get(this));
				ItemUtil.damageItemForUser(pl, stack, hand);
				cast(pl.serverLevel(), stack, pl, data);
				doCastFx(pl.serverLevel(), stack, pl, data);

				return InteractionResults.ItemUse.succeedAndSwingArmOneSide(stack);
			}).orElseGet(() -> InteractionResults.ItemUse.denyUsage(stack));
		}

		return InteractionResultHolder.success(stack);
	}

	public boolean findAndConsumeRunes(Object2IntMap<Item> runes, ServerPlayer player, boolean allowBuffs, ItemStack staff) {
		return ItemUtil.findAndConsumeRunes(runes, player, allowBuffs, staff);
	}

	public Optional<T> checkPreconditions(LivingEntity caster, ItemStack staff) {
		return Optional.of((T)new Object());
	}

	@Nullable
	public SoundEvent getCastingSound() {
		return null;
	}

	public abstract void cast(ServerLevel level, ItemStack staff, LivingEntity caster, T args);
	public void doCastFx(ServerLevel level, ItemStack staff, LivingEntity caster, T args) {}

	@Override
	public void doBlockImpact(BaseEnergyShot shot, Vec3 hitPos, LivingEntity shooter) {}

	@Override
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {return true;}

	@Override
	public InteractionHand getWeaponHand(LivingEntity holder) {
		return holder.getMainHandItem().getItem() == this || holder.getOffhandItem().getItem() != this ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return oldStack.getItem() != newStack.getItem();
	}

	public float getDmg() {
		return 0;
	}

	@Override
	public int getEnchantmentValue() {
		return 8;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (getDmg() > 0)
			tooltip.add(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.MAGIC_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, LocaleUtil.numToComponent(getDmg())));

		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.STAFF_RUNE_COST, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST));

		for (Object2IntMap.Entry<Item> runeEntry : runeCost(stack).runeCosts().object2IntEntrySet()) {
			tooltip.add(LocaleUtil.getLocaleMessage(LocaleUtil.Keys.STAFF_RUNE_COST_LINE, ChatFormatting.WHITE, LocaleUtil.numToComponent(runeEntry.getIntValue()), LocaleUtil.getLocaleMessage(runeEntry.getKey().getDescriptionId())));
		}
	}
}
