package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.EnumSet;
import java.util.List;

public class UtopianArmour extends AdventArmour {
	public UtopianArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.UTOPIAN, slot, new Item.Properties().durability(slot.getDurability(50)).rarity(Rarity.RARE));
	}

	@Override
	public void onEquip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (entity instanceof ServerPlayer pl) {
			float mod = piece == Piece.FULL_SET ? 0.1f : 0.05f;

			PlayerUtil.getAdventPlayer(pl).getSkills().forEach(skill -> skill.applyXpModifier(mod));
		}
	}

	@Override
	public void onUnequip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (entity instanceof ServerPlayer pl) {
			float mod = piece == Piece.FULL_SET ? 0.1f : 0.05f;

			PlayerUtil.getAdventPlayer(pl).getSkills().forEach(skill -> skill.removeXpModifier(mod));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.utopian_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(setEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.utopian_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
