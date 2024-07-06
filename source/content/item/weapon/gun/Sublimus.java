package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Sublimus extends BaseGun {
	public Sublimus(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_GENERIC_FIRE_5.get();
	}

	@Nullable
	@Override
	public BaseBullet findAndConsumeAmmo(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		if (!(shooter instanceof Player pl) || InventoryUtil.findItemForConsumption(pl, getAmmoItem(), pl.getAbilities().instabuild ? 0 : AoAEnchantments.modifyAmmoCost(pl.level(), gunStack, 1), !pl.level().isDay() || !pl.level().canSeeSky(pl.blockPosition())))
			return createProjectileEntity(shooter, gunStack, hand);

		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
