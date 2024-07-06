package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class ArchaicArmour extends AdventArmour {
	public ArchaicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ARCHAIC, slot, 67);
	}

	@Override
	public void handleOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (DamageUtil.isMeleeDamage(ev.getSource()))
			ev.setAmount(ev.getAmount() * (1 + perPieceValue(equippedPieces, 0.1875f) * (1 - EntityUtil.getHealthPercent(entity))));
	}

	@Override
	public void handleIncomingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (!equippedPieces.contains(Piece.FULL_SET))
			ev.setAmount(ev.getAmount() * (1 + perPieceValue(equippedPieces, 0.16f) * (1 - EntityUtil.getHealthPercent(entity))));
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
