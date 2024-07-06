package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.EnumSet;
import java.util.List;

public class ExoplateArmour extends AdventArmour {
	public ExoplateArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.EXOPLATE, slot, 46);
	}

	@Override
	public void handleIncomingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (!DamageUtil.isEnvironmentalDamage(ev.getSource()) && entity.level() instanceof ServerLevel level) {
			int lightLvl = Mth.clamp(2 + WorldUtil.getLightLevel(level, entity.blockPosition(), false, false), 2, 15);

			ev.addReductionModifier(DamageContainer.Reduction.ARMOR, (container, reduction) -> DamageUtil.percentDamageReduction(container, reduction, (1 - (lightLvl / 15f)) * perPieceValue(equippedPieces, 0.0625f)));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.exoplate_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
