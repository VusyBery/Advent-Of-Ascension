package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class HydroplateArmour extends AdventArmour {
	public HydroplateArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.HYDROPLATE, slot, 66);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity.isInWater())
			entity.heal(perPieceValue(equippedPieces, 0.0125f));
	}

	@Override
	public void handleIncomingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (equippedPieces.contains(Piece.FULL_SET) && entity.isInWater() && !DamageUtil.isEnvironmentalDamage(ev.getSource()))
			ev.addReductionModifier(DamageContainer.Reduction.ARMOR, (container, reduction) -> DamageUtil.percentDamageReduction(container, reduction, 0.2f));
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
