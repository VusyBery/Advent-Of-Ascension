package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public record CrossbowStats(float damage) {
    public static final Codec<CrossbowStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("damage").forGetter(CrossbowStats::damage)
    ).apply(builder, CrossbowStats::new));
    public static final StreamCodec<FriendlyByteBuf, CrossbowStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, CrossbowStats::damage,
            CrossbowStats::new);

    public static Item.Properties of(float damage) {
        return new Item.Properties().component(AoADataComponents.CROSSBOW_STATS.get(), new CrossbowStats(damage));
    }
}
