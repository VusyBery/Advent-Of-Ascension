package net.tslat.aoa3.content.item.armour;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;
import java.util.function.BiConsumer;

public class PhantasmArmour extends AdventArmour {
	public static final AttributeModifier PHANTASM_BOOTS_BUFF = new AttributeModifier(AdventOfAscension.id("phantasm_armour_boots"), 1, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier PHANTASM_LEGS_BUFF = new AttributeModifier(AdventOfAscension.id("phantasm_armour_leggings"), 1, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier PHANTASM_CHESTPLATE_BUFF = new AttributeModifier(AdventOfAscension.id("phantasm_armour_chestplate"), 1, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier PHANTASM_HELMET_BUFF = new AttributeModifier(AdventOfAscension.id("phantasm_armour_helmet"), 1, AttributeModifier.Operation.ADD_VALUE);

	public PhantasmArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.PHANTASM, slot, 51);
	}

	@Override
	public void addArmourAttributes(EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> attributes) {
		switch (slot) {
			case FEET -> attributes.accept(Attributes.LUCK, PHANTASM_BOOTS_BUFF);
			case LEGS -> attributes.accept(Attributes.LUCK, PHANTASM_LEGS_BUFF);
			case CHEST -> attributes.accept(Attributes.LUCK, PHANTASM_CHESTPLATE_BUFF);
			case HEAD -> attributes.accept(Attributes.LUCK, PHANTASM_HELMET_BUFF);
			case null, default -> {}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.phantasm_armour.desc.1", LocaleUtil.ItemDescriptionType.UNIQUE));
	}
}
