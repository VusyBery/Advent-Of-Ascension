package net.tslat.aoa3.content.item.armour;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
import java.util.function.BiConsumer;

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
	public void addArmourAttributes(EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> attributes) {
		switch (slot) {
			case FEET -> attributes.accept(Attributes.MOVEMENT_SPEED, BOOTS_BONUS);
			case LEGS -> attributes.accept(Attributes.MOVEMENT_SPEED, LEGS_BONUS);
			case CHEST -> attributes.accept(Attributes.MOVEMENT_SPEED, CHESTPLATE_BONUS);
			case HEAD -> attributes.accept(Attributes.MOVEMENT_SPEED, HELMET_BONUS);
			default -> {}
		}
	}

	@Override
	public void onEquip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (piece == Piece.FULL_SET)
			AttributeUtil.applyTransientModifier(entity, Attributes.MOVEMENT_SPEED, SET_BONUS);
	}

	@Override
	public void onUnequip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (piece == Piece.FULL_SET)
			AttributeUtil.removeModifier(entity, Attributes.MOVEMENT_SPEED, SET_BONUS);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.speed_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
