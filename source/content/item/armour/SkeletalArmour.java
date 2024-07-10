package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class SkeletalArmour extends AdventArmour {
	public SkeletalArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.SKELETAL, slot, 43);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity instanceof ServerPlayer pl && pl.getFoodData().getSaturationLevel() < 1 && equippedPieces.contains(Piece.FULL_SET)) {
			FoodData foodStats = pl.getFoodData();
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
