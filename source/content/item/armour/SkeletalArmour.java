package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class SkeletalArmour extends AdventArmour {
	public SkeletalArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.SKELETAL, slot, 43);
	}

	@Override
	public Type getSetType() {
		return Type.SKELETAL;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots == null && plData.player().getFoodData().getSaturationLevel() < 1) {
			FoodData foodStats = plData.player().getFoodData();
			int foodLvl = foodStats.getFoodLevel();

			foodStats.eat(1, 0.5f);
			foodStats.setFoodLevel(foodLvl);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.skeletal_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
