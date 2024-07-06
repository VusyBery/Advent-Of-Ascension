package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EverfightStaff extends BaseStaff<Float> {
	public EverfightStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_EVER_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.KINETIC_RUNE.get(), 4);
			runes.put(AoAItems.POWER_RUNE.get(), 4);
		});
	}

	public Optional<Float> checkPreconditions(LivingEntity caster, ItemStack staff) {
		float healthPercent = EntityUtil.getHealthPercent(caster);

		return Optional.ofNullable(healthPercent < 1 && healthPercent > 0 ? healthPercent : null);
	}

	@Override
	public void cast(ServerLevel level, ItemStack staff, LivingEntity caster, Float args) {
		EntityUtil.applyPotions(caster, new EffectBuilder(MobEffects.DAMAGE_RESISTANCE, (int)(1200f * (1 - args))));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
