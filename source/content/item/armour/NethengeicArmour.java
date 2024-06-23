package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class NethengeicArmour extends AdventArmour {
	public NethengeicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.NETHENGEIC, slot, 41);
	}

	@Override
	public Type getSetType() {
		return Type.NETHENGEIC;
	}

	@Override
	public void onDamageDealt(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		if (slots == null && plData.player().isOnFire())
			event.setAmount(event.getAmount() * 1.5f);

		if (slots != null && event.getEntity().isOnFire())
			event.setAmount(event.getAmount() * (1 + (0.1f * slots.size())));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nethengeic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.nethengeic_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
