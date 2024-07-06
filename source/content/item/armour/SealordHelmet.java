package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class SealordHelmet extends AdventArmour {
	public static final AttributeModifier SEALORD_ATTACK_BUFF = new AttributeModifier(AdventOfAscension.id("sealord_helmet"), 2, AttributeModifier.Operation.ADD_VALUE);

	public SealordHelmet() {
		super(AoAArmourMaterials.SEALORD_HELMET, ArmorItem.Type.HELMET, 60);
	}

	@Override
	public boolean isCompatibleWithAnySet() {
		return true;
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity.isEyeInFluid(FluidTags.WATER)) {
			AttributeUtil.applyTransientModifier(entity, Attributes.ATTACK_SPEED, SEALORD_ATTACK_BUFF);
		}
		else {
			AttributeUtil.removeModifier(entity, Attributes.ATTACK_SPEED, SEALORD_ATTACK_BUFF);
		}

		if (entity.isInWater())
			entity.setAirSupply(-10);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARMOUR_AIRTIGHT, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(anySetEffectHeader());
	}
}
