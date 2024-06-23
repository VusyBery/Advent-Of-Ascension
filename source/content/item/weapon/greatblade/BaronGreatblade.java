package net.tslat.aoa3.content.item.weapon.greatblade;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.tslat.aoa3.common.registration.item.AoAWeapons;
import net.tslat.aoa3.content.entity.projectile.thrown.GrenadeEntity;
import net.tslat.aoa3.util.ItemUtil;
import net.tslat.aoa3.util.LocaleUtil;

import java.util.List;

public class BaronGreatblade extends BaseGreatblade {
	public BaronGreatblade(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	@Override
	protected void doMeleeEffect(ItemStack stack, LivingEntity target, LivingEntity attacker, float attackCooldown) {
		if (!attacker.level().isClientSide && attackCooldown > 0.85f) {
			if (!(attacker instanceof Player) || ((Player)attacker).isCreative() || ItemUtil.findInventoryItem((Player)attacker, new ItemStack(AoAWeapons.GRENADE.get()), true, 1, false)) {
				attacker.level().addFreshEntity(new GrenadeEntity(attacker, null));
				ItemUtil.damageItem(stack, attacker, 1, EquipmentSlot.MAINHAND);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));
	}
}
