package net.tslat.aoa3.content.item.weapon.staff;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Lightshine extends BaseStaff<List<LivingEntity>> {
	public Lightshine(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getCastingSound() {
		return AoASounds.ITEM_LIGHTSHINE_STAFF_CAST.get();
	}

	public static Object2IntMap<Item> getDefaultRunes() {
		return Util.make(new Object2IntArrayMap<>(), runes -> {
			runes.put(AoAItems.POWER_RUNE.get(), 3);
			runes.put(AoAItems.LIFE_RUNE.get(), 3);
			runes.put(AoAItems.DISTORTION_RUNE.get(), 2);
		});
	}

	@Override
	public Optional<List<LivingEntity>> checkPreconditions(LivingEntity caster, ItemStack staff) {
		List<LivingEntity> targets = EntityRetrievalUtil.getEntities(caster, 10, entity -> entity instanceof LivingEntity livingEntity && EntityUtil.isHostileMob(livingEntity));

		return Optional.ofNullable(targets.isEmpty() ? null : targets);
	}

	@Override
	public void cast(ServerLevel level, ItemStack staff, LivingEntity caster, List<LivingEntity> args) {
		args.removeIf(entity -> !DamageUtil.doMiscMagicAttack(caster, entity, 1, null));
		EntityUtil.healEntity(caster, Math.min(15, args.size()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
