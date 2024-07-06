package net.tslat.aoa3.integration.jei.ingredient.type.imbuing;

import com.google.common.base.MoreObjects;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.common.util.RegistryUtil;
import mezz.jei.common.util.TagUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public record ImbuingIngredientHelper(IIngredientType<EnchantmentInstance> ingredientType) implements IIngredientHelper<EnchantmentInstance> {
    @Override
    public IIngredientType<EnchantmentInstance> getIngredientType() {
        return this.ingredientType;
    }

    @Override
    public String getDisplayName(EnchantmentInstance ingredient) {
        return Enchantment.getFullname(ingredient.enchantment, ingredient.level).getString();
    }

    @Override
    public String getUniqueId(EnchantmentInstance ingredient, UidContext context) {
        return "enchantment:" + getResourceLocation(ingredient) + "_" + ingredient.level;
    }

    @Override
    public ResourceLocation getResourceLocation(EnchantmentInstance ingredient) {
        ResourceLocation id = RegistryUtil.getRegistry(Registries.ENCHANTMENT).getKey(ingredient.enchantment.value());

        if (id == null) {
            String ingredientInfo = getErrorInfo(ingredient);

            throw new IllegalStateException("Found unregistered enchantment: " + ingredientInfo);
        }

        return id;
    }

    @Override
    public ItemStack getCheatItemStack(EnchantmentInstance ingredient) {
        return EnchantedBookItem.createForEnchantment(ingredient);
    }

    @Override
    public EnchantmentInstance copyIngredient(EnchantmentInstance ingredient) {
        return new EnchantmentInstance(ingredient.enchantment, ingredient.level);
    }

    @Override
    public EnchantmentInstance normalizeIngredient(EnchantmentInstance ingredient) {
        return new EnchantmentInstance(ingredient.enchantment, 1);
    }

    @Override
    public Stream<ResourceLocation> getTagStream(EnchantmentInstance ingredient) {
        final Registry<Enchantment> registry = RegistryUtil.getRegistry(Registries.ENCHANTMENT);

        return registry.getResourceKey(ingredient.enchantment.value())
                .flatMap(registry::getHolder)
                .map(Holder::tags)
                .orElse(Stream.of())
                .map(TagKey::location);
    }

    @Override
    public Optional<ResourceLocation> getTagEquivalent(Collection<EnchantmentInstance> ingredients) {
        return TagUtil.getTagEquivalent(ingredients, instance -> instance.enchantment.value(), RegistryUtil.getRegistry(Registries.ENCHANTMENT)::getTags);
    }

    @Override
    public String getErrorInfo(@Nullable EnchantmentInstance ingredient) {
        if (ingredient == null)
            return "null";

        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(ingredient.getClass());
        Enchantment enchant = ingredient.enchantment.value();

        if (enchant != null) {
            toStringHelper.add("Enchantment", getDisplayName(ingredient));
        }
        else {
            toStringHelper.add("Enchantment", "null");
        }

        toStringHelper.add("Level", ingredient.level);

        return toStringHelper.toString();
    }
}
