package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class KnightArmour extends AdventArmour {
	public static final AttributeModifier KNIGHT_BOOTS_BUFF = new AttributeModifier(AdventOfAscension.id("knight_armour_boots"), 1.5d, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier KNIGHT_LEGS_BUFF = new AttributeModifier(AdventOfAscension.id("knight_armour_leggings"), 1.5d, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier KNIGHT_CHESTPLATE_BUFF = new AttributeModifier(AdventOfAscension.id("knight_armour_chestplate"), 1.5d, AttributeModifier.Operation.ADD_VALUE);
	public static final AttributeModifier KNIGHT_HELMET_BUFF = new AttributeModifier(AdventOfAscension.id("knight_armour_helmet"), 1.5d, AttributeModifier.Operation.ADD_VALUE);

	public KnightArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.KNIGHT, slot, 70);
	}

	@Override
	public Type getSetType() {
		return Type.KNIGHT;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots == null && plData.player().isAlive() && EntityUtil.getHealthPercent(plData.player()) < 0.2f)
			plData.player().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 0, 1, false, true));
	}

	@Override
	public void onEquip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		switch (slot) {
			case FEET -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_BOOTS_BUFF);
			case LEGS -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_LEGS_BUFF);
			case CHEST -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_CHESTPLATE_BUFF);
			case HEAD -> AttributeUtil.applyTransientModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_HELMET_BUFF);
			case null, default -> {}
		}
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		switch (slot) {
			case FEET -> AttributeUtil.removeModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_BOOTS_BUFF);
			case LEGS -> AttributeUtil.removeModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_LEGS_BUFF);
			case CHEST -> AttributeUtil.removeModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_CHESTPLATE_BUFF);
			case HEAD -> AttributeUtil.removeModifier(plData.player(), Attributes.MAX_HEALTH, KNIGHT_HELMET_BUFF);
			case null, default -> {}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.knight_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.knight_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
