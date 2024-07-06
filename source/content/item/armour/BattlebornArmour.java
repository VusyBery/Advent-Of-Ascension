package net.tslat.aoa3.content.item.armour;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAArmour;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.util.AttributeUtil;
import net.tslat.aoa3.util.DamageUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.EnumSet;
import java.util.List;

public class BattlebornArmour extends AdventArmour {
	private static final ResourceLocation ATTACK_SPEED_MODIFIER = AdventOfAscension.id("battleborn_armor");

	public BattlebornArmour(ArmorItem.Type slot) {
		super(AoAArmourMaterials.BATTLEBORN, slot, 65);
	}

	private static AttributeModifier createModifier(int cooldownTicks) {
		return new AttributeModifier(ATTACK_SPEED_MODIFIER, Math.min(0.65, cooldownTicks / 240d), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public void handleOutgoingAttack(LivingEntity entity, EnumSet<Piece> equippedPieces, LivingIncomingDamageEvent ev) {
		if (DamageUtil.isMeleeDamage(ev.getSource()) && entity instanceof ServerPlayer pl) {
			int counter = getArmourCooldown(pl);
			int newAmount = Math.min(300, counter + perPieceValue(equippedPieces, 6));

			setArmourCooldown(pl, AoAArmour.BATTLEBORN_ARMOUR, newAmount);
			AttributeUtil.applyTransientModifier(entity, Attributes.ATTACK_SPEED, createModifier(newAmount));
		}
	}

	@Override
	public void onArmourTick(LivingEntity entity, EnumSet<Piece> equippedPieces) {
		if (entity instanceof Player pl) {
			int counter = getArmourCooldown(pl);

			if (counter > 0) {
				if (counter == 1) {
					AttributeUtil.removeModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
				}
				else if (entity.level().getGameTime() % 10 == 0) {
					AttributeUtil.applyTransientModifier(entity, Attributes.ATTACK_SPEED, createModifier(counter));
				}
			}
		}
	}

	@Override
	public void onUnequip(LivingEntity entity, Piece piece, EnumSet<Piece> equippedPieces) {
		if (equippedPieces.size() == 1 && entity instanceof Player pl) {
			int cooldown = getArmourCooldown(pl);

			if (cooldown > 0) {
				AttributeUtil.removeModifier(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
				setArmourCooldown(pl, AoAArmour.BATTLEBORN_ARMOUR, (int)(cooldown * 0.75f));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.battleborn_armour.desc.1", LocaleUtil.ItemDescriptionType.BENEFICIAL));
		tooltip.add(pieceEffectHeader());
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText("item.aoa3.battleborn_armour.desc.2", LocaleUtil.ItemDescriptionType.BENEFICIAL));
	}
}
