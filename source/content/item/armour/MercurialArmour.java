package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class MercurialArmour extends AdventArmour {
	public MercurialArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.MERCURIAL, slot, 42);
	}

	@Override
	public Type getSetType() {
		return Type.MERCURIAL;
	}

	@Override
	public void onPostAttackReceived(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots, LivingDamageEvent event) {
		if (!plData.player().level().isClientSide && event.getSource().is(DamageTypeTags.IS_EXPLOSION) && event.getAmount() > 0) {
			if (slots == null) {
				plData.player().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 320, 1, true, true));
			}
			else if (plData.equipment().getCurrentFullArmourSet() != getSetType()) {
				plData.player().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80 * slots.size(), 0, true, true));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.mercurial_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.mercurial_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.mercurial_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
