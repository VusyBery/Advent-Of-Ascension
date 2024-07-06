package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.EnumSet;
import java.util.List;

public class ElecanyteArmour extends AdventArmour {
	public ElecanyteArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ELECANYTE, slot, 63);
	}

	@Override
	public void afterTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {
		if (ev.getNewDamage() > 0 && entity instanceof ServerPlayer pl && !DamageUtil.isEnvironmentalDamage(ev.getSource()))
			PlayerUtil.addResourceToPlayer(pl, AoAResources.SPIRIT.get(), ev.getNewDamage() * perPieceValue(equippedPieces, 2.5f));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.elecanyte_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.elecanyte_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
