package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class CrystallisArmour extends AdventArmour {
	public CrystallisArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.CRYSTALLIS, slot, 56);
	}

	@Override
	public void afterTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {
		if (ev.getNewDamage() > 0 && ev.getSource().getEntity() instanceof LivingEntity attacker && !ev.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
			if ((equippedPieces.contains(Piece.FULL_SET) && DamageUtil.isRangedDamage(ev.getSource())) || DamageUtil.isMeleeDamage(ev.getSource())) {
				attacker.hurt(attacker.level().damageSources().thorns(entity), ev.getNewDamage() * perPieceValue(equippedPieces, 0.25f));
				entity.hurtArmor(attacker.level().damageSources().generic(), ev.getNewDamage() * 2);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.crystallis_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.crystallis_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.crystallis_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
