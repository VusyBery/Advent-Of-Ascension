package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class WebStaff extends BaseStaff<List<Holder<MobEffect>>> {
	public WebStaff(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_WEB_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.DISTORTION_RUNE.get(), 4);
			runes.put(AoAItems.ENERGY_RUNE.get(), 4);
		});
	}

	@Override
	public Optional<List<Holder<MobEffect>>> checkPreconditions(LivingEntity caster, ItemStack staff) {
		List<Holder<MobEffect>> effects = new ObjectArrayList<>(caster.getActiveEffects().size());

		for (MobEffectInstance effect : caster.getActiveEffects()) {
			if (!effect.getEffect().value().isBeneficial())
				effects.add(effect.getEffect());
		}

		return Optional.ofNullable(effects.isEmpty() ? null : effects);
	}

	@Override
	public void cast(ServerLevel level, ItemStack staff, LivingEntity caster, List<Holder<MobEffect>> args) {
		args.forEach(caster::removeEffect);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
