package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class CandyArmour extends AdventArmour {
	public CandyArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.CANDY, slot, 59);
	}

	@Override
	public Type getSetType() {
		return Type.CANDY;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (plData.player().getFoodData().needsFood()) {
			if (slots == null || plData.equipment().isCooledDown("candy_armour")) {
				if (findAndConsumeFood(plData.player()))
					plData.equipment().setCooldown("candy_armour", 12000 / (slots == null ? 4 : slots.size()));
			}
		}
	}

	private boolean findAndConsumeFood(Player player) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			FoodProperties foodProperties = stack.getFoodProperties(player);

			if (foodProperties != null && foodProperties.nutrition() > 0 && foodProperties.saturation() > 0) {
				player.getInventory().setItem(i, stack.getItem().finishUsingItem(stack, player.level(), player));

				return true;
			}
		}

		return false;
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
