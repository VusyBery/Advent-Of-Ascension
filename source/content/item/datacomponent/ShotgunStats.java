package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public record ShotgunStats(int pellets, float knockbackModifier) {
    public static final Codec<ShotgunStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("pellets").forGetter(ShotgunStats::pellets),
            Codec.FLOAT.fieldOf("knockback_modifier").forGetter(ShotgunStats::knockbackModifier)
    ).apply(builder, ShotgunStats::new));
    public static final StreamCodec<FriendlyByteBuf, ShotgunStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ShotgunStats::pellets,
            ByteBufCodecs.FLOAT, ShotgunStats::knockbackModifier,
            ShotgunStats::new);

    public static Item.Properties of(Item.Properties properties, int pellets, float knockbackModifier) {
        return properties.component(AoADataComponents.SHOTGUN_STATS.get(), new ShotgunStats(pellets, knockbackModifier));
    }
}
