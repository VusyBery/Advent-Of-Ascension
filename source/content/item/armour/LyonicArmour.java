package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.EntityUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.EnumSet;
import java.util.List;

public class LyonicArmour extends AdventArmour {
	public LyonicArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.LYONIC, slot, 56);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity.level().getGameTime() % 2 == 0) {
			final boolean xpOrbs = equippedPieces.contains(Piece.FULL_SET);

			EntityRetrievalUtil.<Entity>getEntities(entity, perPieceValue(equippedPieces, 1.5f), entity2 -> entity2.isAlive() && (entity2 instanceof ItemEntity item && !item.getItem().isEmpty() && !item.hasPickUpDelay() && canPullItem(item)) || (xpOrbs && entity2 instanceof ExperienceOrb)).stream()
					.limit(200)
					.forEach(entity2 -> EntityUtil.pullEntityIn(entity, entity2, 0.05f, true));
		}
	}

	private boolean canPullItem(ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty() && !item.hasPickUpDelay();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.lyonic_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.lyonic_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.lyonic_armour.desc.3", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
