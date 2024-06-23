package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class HydroplateArmour extends AdventArmour {
	public HydroplateArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.HYDROPLATE, slot, 66);
	}

	@Override
	public Type getSetType() {
		return Type.HYDROPLATE;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots != null && plData.player().isInWater())
			plData.player().heal(0.0125f * (float)slots.size());
	}

	@Override
	public void onAttackReceived(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots == null && plData.player().isInWater() && !DamageUtil.isEnvironmentalDamage(event.getSource()))
			event.setAmount(event.getAmount() * 0.8f);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.hydroplate_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.hydroplate_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.hydroplate_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
