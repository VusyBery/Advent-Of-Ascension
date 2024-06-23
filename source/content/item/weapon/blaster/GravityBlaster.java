package net.tslat.aoa3.content.item.weapon.blaster;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GravityBlaster extends BaseBlaster {
	public GravityBlaster(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GRAVITY_BLASTER_FIRE.get();
	}

	@Override
	public void fireBlaster(ServerLevel level, LivingEntity shooter, ItemStack blaster) {
		ItemStack stack = shooter.getItemInHand(InteractionHand.MAIN_HAND);

		if (!stack.is(this))
			stack = getDefaultInstance();

		for (LivingEntity mob : shooter.level().getEntitiesOfClass(LivingEntity.class, shooter.getBoundingBox().inflate(2, 0, 2), EntityUtil::isHostileMob)) {
			EntityUtil.pushEntityAway(shooter, mob, 0.5f);
			DamageUtil.doMiscEnergyAttack(shooter, mob, getBlasterDamage(stack), null);
		}

		shooter.hurtMarked = true;
		shooter.push(0, 2f, 0);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
