package net.tslat.aoa3.content.item.armour;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PurityArmour extends AdventArmour {
	private static final Supplier<Set<Holder<MobEffect>>> IMMUNE_EFFECTS = Suppliers.memoize(() -> Set.of(MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.BLINDNESS, MobEffects.CONFUSION));

	public PurityArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.PURITY, slot, 61);
	}

	@Override
	public void onEquip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (!entity.getActiveEffectsMap().isEmpty()) {
			for (Holder<MobEffect> effect : IMMUNE_EFFECTS.get()) {
				if (entity.hasEffect(effect))
					entity.removeEffect(effect);
			}
		}
	}

	@Override
	public void onEffectApplication(LivingEntity entity, EnumSet<Piece> equippedPieces, MobEffectEvent.Applicable ev) {
		if (IMMUNE_EFFECTS.get().contains(ev.getEffectInstance().getEffect()))
			ev.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.purity_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
