package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class SealordHelmet extends AdventArmour {
	public static final AttributeModifier SEALORD_ATTACK_BUFF = new AttributeModifier(AdventOfAscension.id("sealord_helmet"), 2, AttributeModifier.Operation.ADD_VALUE);

	public SealordHelmet() {
		super(AoAArmourMaterials.SEALORD_HELMET, ArmorItem.Type.HELMET, 60);
	}

	@Override
	public Type getSetType() {
		return Type.ALL;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		Player player = plData.player();

		if (player.isEyeInFluid(FluidTags.WATER)) {
			AttributeUtil.applyTransientModifier(plData.player(), Attributes.ATTACK_SPEED, SEALORD_ATTACK_BUFF);
		}
		else {
			AttributeUtil.removeModifier(plData.player(), Attributes.ATTACK_SPEED, SEALORD_ATTACK_BUFF);
		}

		if (plData.player().isInWater())
			plData.player().setAirSupply(-10);
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		AttributeUtil.removeModifier(plData.player(), Attributes.ATTACK_SPEED, SEALORD_ATTACK_BUFF);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARMOUR_AIRTIGHT, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
		tooltip.add(anySetEffectHeader());
	}
}
