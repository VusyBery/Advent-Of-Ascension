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
import java.util.UUID;

public class SubterraneanArmour extends AdventArmour {
	private static final AttributeModifier ATTACK_SPEED_DEBUFF = new AttributeModifier(AdventOfAscension.id("subterranean_armor_debuff"), -0.16666667, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

	public SubterraneanArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.SUBTERRANEAN, slot, 47);
	}

	@Override
	public Type getSetType() {
		return Type.SUBTERRANEAN;
	}

	@Override
	public void onEquip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		if (slot == null)
			AttributeUtil.applyTransientModifier(plData.player(), Attributes.ATTACK_SPEED, ATTACK_SPEED_DEBUFF);
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		if (slot == null)
			AttributeUtil.removeModifier(plData.player(), Attributes.ATTACK_SPEED, ATTACK_SPEED_DEBUFF);
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots == null)
			plData.player().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 0, 1, true, false));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.subterranean_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
