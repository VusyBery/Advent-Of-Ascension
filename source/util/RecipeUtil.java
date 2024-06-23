package net.tslat.aoa3.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
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
}
