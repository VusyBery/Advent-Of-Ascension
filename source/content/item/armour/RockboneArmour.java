package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.AdvancementUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.EnchantmentUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class RockboneArmour extends AdventArmour {
	public RockboneArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ROCKBONE, slot, 45);
	}

	@Override
	public Type getSetType() {
		return Type.ROCKBONE;
	}

	@Override
	public void onPreAttackReceived(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingAttackEvent event) {
		if (slots == null && RandomUtil.oneInNChance(10) && DamageUtil.isRangedDamage(event.getSource())) {
			event.setCanceled(true);

			for (ItemStack armour : plData.player().getArmorSlots()) {
				if (EnchantmentUtil.getEnchantmentLevel(plData.player().level(), armour, Enchantments.PROJECTILE_PROTECTION) < 4)
					return;
			}

			AdvancementUtil.grantCriterion(plData.player(), AdventOfAscension.id("completionist/reverse_stormtrooper"), "max_dodge");
		}
	}

	@Override
	public void onAttackReceived(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots != null && DamageUtil.isRangedDamage(event.getSource()))
			event.setAmount(event.getAmount() * (1 - (0.1f * slots.size())));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.rockbone_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.rockbone_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
