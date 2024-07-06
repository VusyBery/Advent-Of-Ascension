package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import net.tslat.effectslib.api.util.EffectBuilder;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.EnumSet;
import java.util.List;

public class BoreicArmour extends AdventArmour {
	public BoreicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.BOREIC, slot, 62);
	}

	@Override
	public void afterTakingDamage(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingDamageEvent.Post ev) {
		if (ev.getNewDamage() > 0 && entity.isInWater() && !DamageUtil.isEnvironmentalDamage(ev.getSource())) {
			WorldUtil.createExplosion(entity, entity.level(), entity.blockPosition(), 0.7f + perPieceValue(equippedPieces, 0.3f));

			if (equippedPieces.contains(Piece.FULL_SET)) {
				for (LivingEntity entity2 : EntityRetrievalUtil.<LivingEntity>getEntities(entity, 4, entity2 -> entity2 instanceof LivingEntity && EntityUtil.isHostileMob(entity2))) {
					entity2.addEffect(new EffectBuilder(MobEffects.MOVEMENT_SLOWDOWN, 40).level(2).isAmbient().build());
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.boreic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.boreic_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.boreic_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
