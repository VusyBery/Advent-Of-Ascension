package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.entity.projectile.staff.MeteorFallEntity;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MeteorStaff extends BaseStaff<BlockPos> {
	public MeteorStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_METEOR_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.COMPASS_RUNE.get(), 1);
			runes.put(AoAItems.LUNAR_RUNE.get(), 2);
			runes.put(AoAItems.KINETIC_RUNE.get(), 2);
		});
	}

	@Override
	public Optional<BlockPos> checkPreconditions(LivingEntity caster, ItemStack staff) {
		return Optional.ofNullable(PlayerUtil.getBlockAimingAt(caster, 70));
	}

	@Override
	public void cast(Level world, ItemStack staff, LivingEntity caster, BlockPos args) {
		for (int i = 0; i < 8; i++) {
			world.addFreshEntity(new MeteorFallEntity(caster, this, (args.getX() - 4) + RandomUtil.randomValueUpTo(8), args.getY() + 30, (args.getZ() - 4) + RandomUtil.randomValueUpTo(8), 3.0f));
		}
	}

	@Override
	public void doBlockImpact(BaseEnergyShot shot, Vec3 hitPos, LivingEntity shooter) {
		WorldUtil.createExplosion(shooter, shot.level(), shot, 1.4f);
	}

	@Override
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {
		if (DamageUtil.doMagicProjectileAttack(shooter, shot, target, getDmg())) {
			WorldUtil.createExplosion(shooter, shot.level(), shot, 1.4f);

			return true;
		}

		return false;
	}

	@Override
	public float getDmg() {
		return 28;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
