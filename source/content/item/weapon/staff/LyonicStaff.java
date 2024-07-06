package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.entity.projectile.staff.LyonicShotEntity;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LyonicStaff extends BaseStaff<List<LivingEntity>> {
	public LyonicStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.ENERGY_RUNE.get(), 1);
			runes.put(AoAItems.WIND_RUNE.get(), 1);
			runes.put(AoAItems.WITHER_RUNE.get(), 2);
			runes.put(AoAItems.STRIKE_RUNE.get(), 1);
		});
	}

	@Override
	public Optional<List<LivingEntity>> checkPreconditions(LivingEntity caster, ItemStack staff) {
		List<LivingEntity> targets = EntityRetrievalUtil.getEntities(caster, 10, 1, 10, entity -> entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && EntityUtil.isHostileMob(livingEntity));

		return Optional.ofNullable(targets.isEmpty() ? null : targets);
	}

	@Override
	public void cast(ServerLevel level, ItemStack staff, LivingEntity caster, List<LivingEntity> args) {
		for (float x = -1; x <= 1; x += 0.125f) {
			for (float z = -1; z <= 1; z += 0.125f) {
				level.addFreshEntity(new LyonicShotEntity(caster, this, 1, x, 0, z));
			}
		}
	}

	@Override
	public boolean doEntityImpact(BaseEnergyShot shot, Entity target, LivingEntity shooter) {
		if (EntityUtil.isHostileMob(target) && target.level() instanceof ServerLevel) {
			EntityUtil.applyPotions(target, new EffectBuilder(MobEffects.WITHER, 100).level(2));

			if (RandomUtil.oneInNChance(150))
				WorldUtil.spawnLightning((ServerLevel)target.level(), shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null, target.getX(), target.getY(), target.getZ(), true, false);

			return true;
		}

		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.WITHERS_TARGETS, LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 2));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
