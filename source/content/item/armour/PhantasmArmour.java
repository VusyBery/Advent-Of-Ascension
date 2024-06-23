package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class PhantasmArmour extends AdventArmour {
	public PhantasmArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.PHANTASM, slot, 51);
	}

	@Override
	public Type getSetType() {
		return Type.PHANTASM;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots != null)
			plData.player().addEffect(new MobEffectInstance(MobEffects.LUCK, 0, slots.size() - 1, true, false));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.phantasm_armour.desc.1", LocaleUtil.ItemDescriptionType.UNIQUE));
	}
}
