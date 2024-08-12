package net.tslat.aoa3.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.function.Function;

public final class RecipeUtil {
    public record RecipeBookDetails(String group, CraftingBookCategory category, boolean showUnlockNotification) {
        public static <T extends Recipe<?>> Products.P3<RecordCodecBuilder.Mu<T>, String, CraftingBookCategory, Boolean> codec(RecordCodecBuilder.Instance<T> builder, Function<T, RecipeBookDetails> getter) {
            return builder.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(instance -> getter.apply(instance).group),
                    CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(instance -> getter.apply(instance).category),
                    Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(instance -> getter.apply(instance).showUnlockNotification));
        }
        public static StreamCodec<RegistryFriendlyByteBuf, RecipeBookDetails> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, RecipeBookDetails::group,
                NeoForgeStreamCodecs.enumCodec(CraftingBookCategory.class), RecipeBookDetails::category,
                ByteBufCodecs.BOOL, RecipeBookDetails::showUnlockNotification,
                RecipeBookDetails::new);
    }

    public static <R extends Recipe<?>> RecordCodecBuilder<R, NonNullList<Ingredient>> shapelessIngredientCodec(String recipeTypeName, Function<R, NonNullList<Ingredient>> getter) {
        return shapelessIngredientCodec(recipeTypeName, "ingredients", ShapedRecipePattern.getMaxWidth() * ShapedRecipePattern.getMaxHeight(), getter);
    }

    public static <R extends Recipe<?>> RecordCodecBuilder<R, NonNullList<Ingredient>> shapelessIngredientCodec(String recipeTypeName, String fieldName, int maxIngredients, Function<R, NonNullList<Ingredient>> getter) {
        return Ingredient.CODEC_NONEMPTY
                .listOf()
                .fieldOf(fieldName)
                .flatXmap(ingredients -> {
                    final Ingredient[] ingredientArray = ingredients.toArray(Ingredient[]::new);

                    if (ingredientArray.length == 0)
                        return DataResult.error(() -> "No ingredients for " + recipeTypeName + " recipe");

                    return ingredientArray.length > maxIngredients
                            ? DataResult.error(() -> "Too many ingredients for " + recipeTypeName + " recipe. The maximum is: %s".formatted(maxIngredients))
                            : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredientArray));
                    }, DataResult::success)
                .forGetter(getter);
    }
}
