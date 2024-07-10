package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.player.resource.SpiritResource;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.EnumSet;
import java.util.List;

public class EmbrodiumArmour extends AdventArmour {
	public EmbrodiumArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.EMBRODIUM, slot, 45);
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity instanceof ServerPlayer player) {
			float perTickRegen = PlayerUtil.getAdventPlayer(player).getResource(AoAResources.SPIRIT.get()) instanceof SpiritResource spirit ? spirit.getPerTickRegen() : 0.04f;
			float amount = equippedPieces.contains(Piece.FULL_SET) ? perTickRegen * 0.25f : 0;
			float temp = WorldUtil.getAmbientTemperature(player.level(), player.blockPosition());

			if (temp > 0.8f)
				amount += perPieceValue(equippedPieces, perTickRegen * 0.25f) * Math.min(1f, (temp / 2f));

			if (amount > 0)
				PlayerUtil.addResourceToPlayer(player, AoAResources.SPIRIT.get(), amount);
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