package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.EnumSet;
import java.util.List;

public class HazmatArmour extends AdventArmour {
	public HazmatArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.HAZMAT, slot, 30);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (equippedPieces.contains(Piece.FULL_SET) && entity.getAirSupply() < entity.getMaxAirSupply())
			entity.setAirSupply(entity.getMaxAirSupply());
	}

	@Override
	public boolean isHelmetAirTight(Player player) {
		return PlayerUtil.isWearingFullSet(player, getMaterial());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARMOUR_AIRTIGHT, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO));
	}
}
