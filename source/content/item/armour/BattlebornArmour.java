package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class BattlebornArmour extends AdventArmour {
	private static final ResourceLocation ATTACK_SPEED_MODIFIER = AdventOfAscension.id("battleborn_armor");

	public BattlebornArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.BATTLEBORN, slot, 65);
	}

	@Override
	public Type getSetType() {
		return Type.BATTLEBORN;
	}

	private static AttributeModifier createModifier(double currentValue) {
		return new AttributeModifier(ATTACK_SPEED_MODIFIER, currentValue, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void onDamageDealt(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots != null && DamageUtil.isMeleeDamage(event.getSource())) {
			int counter = plData.equipment().getCooldown("battleborn");
			int newAmount = Math.min(300, counter + slots.size() * 6);

			plData.equipment().setCooldown("battleborn", newAmount);
			AttributeUtil.applyTransientModifier(plData.player(), Attributes.ATTACK_SPEED, createModifier(Math.min(0.65, newAmount / 240d)));
		}
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		int counter = plData.equipment().getCooldown("battleborn");

		if (counter == 1) {
			AttributeUtil.removeModifier(plData.player(), Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
		}
		else if (counter > 0 && plData.player().level().getGameTime() % 10 == 0) {
			AttributeUtil.applyTransientModifier(plData.player(), Attributes.ATTACK_SPEED, createModifier(Math.min(0.65, counter / 240d)));
		}
	}

	@Override
	public void onUnequip(ServerPlayerDataManager plData, @Nullable EquipmentSlot slot) {
		if (slot != null) {
			int cooldown = plData.equipment().getCooldown("battleborn");

			if (cooldown > 0) {
				AttributeUtil.removeModifier(plData.player(), Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
				plData.equipment().setCooldown("battleborn", (int)(cooldown * 0.75f));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.battleborn_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.battleborn_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
