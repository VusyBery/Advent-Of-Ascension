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

import java.util.EnumSet;
import java.util.List;

public class LunarArmour extends AdventArmour {
	public LunarArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.LUNAR, slot, 63);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2, perPieceValue(equippedPieces, 1) - 1, true, false));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.lunar_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.lunar_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
