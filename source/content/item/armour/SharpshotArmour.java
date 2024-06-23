package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.content.item.weapon.gun.BaseGun;
import net.tslat.aoa3.content.item.weapon.sniper.BaseSniper;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class SharpshotArmour extends AdventArmour {
	public SharpshotArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.SHARPSHOT, slot, 54);
	}

	@Override
	public Type getSetType() {
		return Type.SHARPSHOT;
	}

	@Override
	public void onDamageDealt(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingHurtEvent event) {
		Item gun;

		if (DamageUtil.isGunDamage(event.getSource()) && ((gun = plData.player().getMainHandItem().getItem()) instanceof BaseGun || (gun = plData.player().getOffhandItem().getItem()) instanceof BaseGun)) {
			if (slots == null) {
				if (gun instanceof BaseSniper)
					event.setAmount(event.getAmount() * 1.1f);
			}
			else {
				event.setAmount(event.getAmount() * (1 + (0.07f * slots.size())));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.sharpshot_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.sharpshot_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
