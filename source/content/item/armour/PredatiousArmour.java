package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class PredatiousArmour extends AdventArmour {
	public PredatiousArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.PREDATIOUS, slot, 51);
	}

	@Override
	public void handleOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (DamageUtil.isRangedDamage(ev.getSource()))
			ev.setAmount(ev.getAmount() * (1f + perPieceValue(equippedPieces, 0.1f)));
	}

	@Override
	public void afterTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {
		if (equippedPieces.contains(Piece.FULL_SET) && DamageUtil.isMeleeDamage(ev.getSource()) && ev.getSource().getDirectEntity() instanceof LivingEntity attacker)
			attacker.hurt(entity.level().damageSources().thorns(entity), 1);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.predatious_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.predatious_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
