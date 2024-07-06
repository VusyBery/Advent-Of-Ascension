package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.EnumSet;
import java.util.List;

public class CommanderArmour extends AdventArmour {
	public CommanderArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.COMMANDER, slot, 62);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity.tickCount % 20 == 0) {
			for (LivingEntity target : EntityRetrievalUtil.<LivingEntity>getEntities(entity, perPieceValue(equippedPieces, 2), target -> target != entity && target instanceof LivingEntity && PlayerUtil.getPlayerOrOwnerIfApplicable(target) == entity)) {
				target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 25, equippedPieces.contains(Piece.FULL_SET) ? 1 : 0, false, true));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.commander_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.commander_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.commander_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
