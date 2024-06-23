package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class GhastlyArmour extends AdventArmour {
	public GhastlyArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.GHASTLY, slot, 62);
	}

	@Override
	public Type getSetType() {
		return Type.GHASTLY;
	}

	@Override
	public void onEffectTick(ServerPlayerDataManager plData, @Nullable HashSet<EquipmentSlot> slots) {
		if (slots != null && plData.player().level().getGameTime() % 5 == 0 && plData.player().isShiftKeyDown()) {
			for (LivingEntity entity : plData.player().level().getEntitiesOfClass(LivingEntity.class, plData.player().getBoundingBox().inflate(4 * slots.size()), EntityUtil::isHostileMob)) {
				entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 6, 0, true, false));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.ghastly_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.ghastly_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
