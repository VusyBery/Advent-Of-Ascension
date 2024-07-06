package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LunarAssaultRifle extends BaseGun {
	private final double minDamage;
	private final double maxDamage;

	public LunarAssaultRifle(Item.Properties properties) {
		super(properties);

		final float baseDamage = getGunStats(getDefaultInstance()).damage();
		this.minDamage = baseDamage - (baseDamage / 2d);
		this.maxDamage = baseDamage + (baseDamage / 2d);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_GENERIC_FIRE_3.get();
	}

	@Override
	public float getGunDamage(ItemStack stack) {
		return (float)RandomUtil.randomValueBetween(this.minDamage, this.maxDamage);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);

		tooltip.set(1, LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.RANDOM_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, LocaleUtil.numToComponent(this.minDamage), LocaleUtil.numToComponent(this.maxDamage)));
	}
}
