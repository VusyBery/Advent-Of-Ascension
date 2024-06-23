package net.tslat.aoa3.content.item.weapon.crossbow;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.tslat.aoa3.content.entity.projectile.arrow.CustomArrowEntity;
import net.tslat.aoa3.util.EnchantmentUtil;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpectralCrossbow extends BaseCrossbow {
	public SpectralCrossbow(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemStack getAmmoStack(LivingEntity shooter, ItemStack crossbowStack) {
		return new ItemStack(Items.ARROW, Math.max(1, EnchantmentUtil.getEnchantmentLevel(shooter.level(), crossbowStack, Enchantments.MULTISHOT)));
	}

	@Override
	public CustomArrowEntity applyArrowMods(CustomArrowEntity arrow, @Nullable Entity shooter, ItemStack stack, boolean isCritical) {
		arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

		return arrow;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(LocaleUtil.Keys.ARROW_DAMAGE, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, Component.literal(Double.toString(getCrossbowDamage(stack)))));
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.BENEFICIAL, 1));

		List<ItemStack> projectiles = stack.get(DataComponents.CHARGED_PROJECTILES).getItems();

		if (isCharged(stack) && !projectiles.isEmpty()) {
			ItemStack projectile = projectiles.get(0);
			tooltip.add((Component.translatable("item.minecraft.crossbow.projectile")).append(" ").append(projectile.getDisplayName()));

			if (flag.isAdvanced() && projectile.getItem() == Items.FIREWORK_ROCKET) {
				List<Component> list1 = Lists.newArrayList();
				Items.FIREWORK_ROCKET.appendHoverText(projectile, context, list1, flag);

				if (!list1.isEmpty()) {
                    list1.replaceAll(element -> (Component.literal("  ")).append(element).withStyle(ChatFormatting.GRAY));

					tooltip.addAll(list1);
				}
			}
		}
	}
}
