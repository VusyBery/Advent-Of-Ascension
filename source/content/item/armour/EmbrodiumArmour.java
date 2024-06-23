package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class EmbrodiumArmour extends AdventArmour {
	public EmbrodiumArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.EMBRODIUM, slot, 45);
	}

	@Override
	public Type getSetType() {
		return Type.EMBRODIUM;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots == null) {
			plData.getResource(AoAResources.SPIRIT.get()).addValue(0.08f);
		}
		else {
			Player pl = plData.player();
			float temp = WorldUtil.getAmbientTemperature(pl.level(), pl.blockPosition());

			if (temp > 0.8f)
				plData.getResource(AoAResources.SPIRIT.get()).addValue(0.08f * slots.size() * Math.min(1f, (temp / 2f)));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.embrodium_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.embrodium_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}