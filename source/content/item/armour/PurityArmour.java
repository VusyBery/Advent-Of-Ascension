package net.tslat.aoa3.content.item.armour;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class PurityArmour extends AdventArmour {
	public PurityArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.PURITY, slot, 61);
	}

	@Override
	public Type getSetType() {
		return Type.PURITY;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots == null && !plData.player().getActiveEffectsMap().isEmpty())
			checkAndRemoveEffects(plData.player(), MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.BLINDNESS, MobEffects.CONFUSION);
	}

	private void checkAndRemoveEffects(Player pl, Holder<MobEffect>... effects) {
		for (Holder<MobEffect> effect : effects) {
			if (pl.hasEffect(effect))
				pl.removeEffect(effect);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.purity_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
