package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedArmour extends AdventArmour {
	private static final AttributeModifier SET_BONUS = new AttributeModifier(AdventOfAscension.id("speed_armour_set"), 0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	private static final AttributeModifier BOOTS_BONUS = new AttributeModifier(AdventOfAscension.id("speed_armour_boots"), 0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	private static final AttributeModifier LEGS_BONUS = new AttributeModifier(AdventOfAscension.id("speed_armour_leggings"), 0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	private static final AttributeModifier CHESTPLATE_BONUS = new AttributeModifier(AdventOfAscension.id("speed_armour_chestplate"), 0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	private static final AttributeModifier HELMET_BONUS = new AttributeModifier(AdventOfAscension.id("speed_armour_helmet"), 0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	public SpeedArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.SPEED, slot, 63);
	}

	@Override
	public Type getSetType() {
		return Type.SPEED;
	}

	@Override
	public void onEquip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		switch (slot) {
			case FEET -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MOVEMENT_SPEED, BOOTS_BONUS);
			case LEGS -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MOVEMENT_SPEED, LEGS_BONUS);
			case CHEST -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MOVEMENT_SPEED, CHESTPLATE_BONUS);
			case HEAD -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MOVEMENT_SPEED, HELMET_BONUS);
			case null -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MOVEMENT_SPEED, SET_BONUS);
			default -> {}
		}
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		switch (slot) {
			case FEET -> AttributeUtil.removeModifier(plData.player(), Attributes.MOVEMENT_SPEED, BOOTS_BONUS);
			case LEGS -> AttributeUtil.removeModifier(plData.player(), Attributes.MOVEMENT_SPEED, LEGS_BONUS);
			case CHEST -> AttributeUtil.removeModifier(plData.player(), Attributes.MOVEMENT_SPEED, CHESTPLATE_BONUS);
			case HEAD -> AttributeUtil.removeModifier(plData.player(), Attributes.MOVEMENT_SPEED, HELMET_BONUS);
			case null -> AttributeUtil.removeModifier(plData.player(), Attributes.MOVEMENT_SPEED, SET_BONUS);
			default -> {}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.speed_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.speed_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
