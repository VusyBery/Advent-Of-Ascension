package net.tslat.aoa3.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public final class EnchantmentUtil {
    public static boolean hasEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return hasEnchantment(stack, toHolder(level, enchantment));
    }

    public static boolean hasEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return getEnchantmentLevel(stack, enchantment) > 0;
    }

    public static int getEnchantmentLevel(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return getEnchantmentLevel(stack, toHolder(level, enchantment));
    }

    public static int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        return stack.getEnchantmentLevel(enchantment);
    }

    public static void removeEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        final Holder<Enchantment> enchant = toHolder(level, enchantment);

        EnchantmentHelper.updateEnchantments(stack, enchants -> enchants.removeIf(enchant::is));
    }

    public static void addEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment) {
        addEnchantment(level, stack, enchantment, 1);
    }

    public static void addEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment, int enchantLevel) {
        addEnchantment(level, stack, enchantment, enchantLevel, false);
    }

    public static void addEnchantment(Level level, ItemStack stack, ResourceKey<Enchantment> enchantment, int enchantLevel, boolean ignoreRestrictions) {
        stack.enchant(toHolder(level, enchantment), enchantLevel);
    }

    public static Holder<Enchantment> toHolder(Level level, ResourceLocation enchantment) {
        return toHolder(level, ResourceKey.create(Registries.ENCHANTMENT, enchantment));
    }

    public static Holder<Enchantment> toHolder(Level level, ResourceKey<Enchantment> enchantment) {
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(enchantment);
    }

    public static Component getFormattedName(Holder<Enchantment> enchantment) {
        return getFormattedName(enchantment, 1);
    }

    public static Component getFormattedName(Holder<Enchantment> enchantment, int level) {
        return Enchantment.getFullname(enchantment, level);
    }
}
