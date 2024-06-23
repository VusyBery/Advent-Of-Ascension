package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.entity.projectile.staff.FireflyShotEntity;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FireflyStaff extends BaseStaff<Object> {
	public FireflyStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_FIREFLY_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.WIND_RUNE.get(), 2);
			runes.put(AoAItems.STRIKE_RUNE.get(), 2);
			runes.put(AoAItems.FIRE_RUNE.get(), 1);
		});
	}

	@Override
	public void cast(Level world, ItemStack staff, LivingEntity caster, Object args) {
		world.addFreshEntity(new FireflyShotEntity(caster, this, 60));
	}

	@Override
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {
		if (DamageUtil.doMagicProjectileAttack(shooter, shot, target, getDmg())) {
			target.igniteForSeconds(5);

			UUID targetUUID = target.getUUID();

			if (targetUUID.equals(((FireflyShotEntity)shot).lastTargetUUID))
				return true;

			for (int i = 0; i < RandomUtil.randomNumberBetween(1, 7); i++) {
				shot.level().addFreshEntity(new FireflyShotEntity(shooter, this, (FireflyShotEntity)shot, targetUUID, RandomUtil.randomScaledGaussianValue(0.35f), 1.4f, RandomUtil.randomScaledGaussianValue(0.35f)));
			}

			return true;
		}

		return false;
	}

	@Override
	public float getDmg() {
		return 22;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
