package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class NethengeicArmour extends AdventArmour {
	public NethengeicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.NETHENGEIC, slot, 41);
	}

	@Override
	public void handleOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (equippedPieces.contains(Piece.FULL_SET) && entity.isOnFire())
			ev.setAmount(ev.getAmount() * 1.5f);

		if (ev.getEntity().isOnFire())
			ev.setAmount(ev.getAmount() * (1 + perPieceValue(equippedPieces, 0.1f)));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nethengeic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nethengeic_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
