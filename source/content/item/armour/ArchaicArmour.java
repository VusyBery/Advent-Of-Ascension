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
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class ArchaicArmour extends AdventArmour {
	public ArchaicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ARCHAIC, slot, 67);
	}

	@Override
	public void onDamageDealt(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots != null && DamageUtil.isMeleeDamage(event.getSource()))
			event.setAmount(event.getAmount() * (1 + 0.1875f * slots.size() * (1 - EntityUtil.getHealthPercent(plData.player()))));
	}

	@Override
	public void onAttackReceived(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots != null && plData.equipment().getCurrentFullArmourSet() != getSetType() && DamageUtil.isMeleeDamage(event.getSource()))
			event.setAmount(event.getAmount() * (1 + 0.16f * slots.size() * (1 - EntityUtil.getHealthPercent(plData.player()))));
	}

	@Override
	public Type getSetType() {
		return Type.ARCHAIC;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.archaic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.archaic_armour.desc.2", LocaleUtil.ItemDescriptionType.HARMFUL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.archaic_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
