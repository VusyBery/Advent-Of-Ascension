package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmour;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class CandyArmour extends AdventArmour {
	public CandyArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.CANDY, slot, 59);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity instanceof ServerPlayer pl && pl.getFoodData().needsFood()) {
			if (equippedPieces.contains(Piece.FULL_SET) || !isOnCooldown(pl)) {
				if (findAndConsumeFood(pl))
					setArmourCooldown(pl, AoAArmour.CANDY_ARMOUR, 12000 / perPieceValue(equippedPieces, 1));
			}
		}
	}

	private boolean findAndConsumeFood(Player player) {
		return InventoryUtil.findItem(player, stack -> {
			FoodProperties properties = stack.getFoodProperties(player);

			return properties != null && properties.nutrition() > 0 && properties.saturation() > 0;
		}).map(pair -> {
			player.getInventory().setItem(pair.leftInt(), pair.right().getItem().finishUsingItem(pair.right(), player.level(), player));

			return true;
		}).orElse(false);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.candy_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.candy_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.candy_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
