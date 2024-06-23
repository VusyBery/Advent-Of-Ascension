package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.library.object.Text;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class AlacrityArmour extends AdventArmour {
	public AlacrityArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.ALACRITY, slot, 55);
	}

	@Override
	public Type getSetType() {
		return Type.ALACRITY;
	}

	@Override
	public void onPlayerLandingFall(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingFallEvent event) {
		if (slots == null) {
			event.setCanceled(true);
		}
		else if (plData.equipment().getCurrentFullArmourSet() != getSetType()) {
			event.setDamageMultiplier(1 - (slots.size() * 0.2f));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(Text.formatAsBeneficial(LocaleUtil.itemDescKey(AdventOfAscension.id("alacrity_armour"), 1)));
		tooltip.add(setEffectHeader());
		tooltip.add(Text.formatAsBeneficial(LocaleUtil.itemDescKey(AdventOfAscension.id("alacrity_armour"), 2)));
	}
}
