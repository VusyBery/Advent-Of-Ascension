package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public record BowStats(float damage, float drawSpeedModifier) {
    public static final Codec<BowStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("damage").forGetter(BowStats::damage),
            Codec.FLOAT.fieldOf("draw_speed_modifier").forGetter(BowStats::drawSpeedModifier)
    ).apply(builder, BowStats::new));
    public static final StreamCodec<FriendlyByteBuf, BowStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BowStats::damage,
            ByteBufCodecs.FLOAT, BowStats::drawSpeedModifier,
            BowStats::new);

    public static Item.Properties of(float damage, float drawSpeedModifier) {
        return new Item.Properties().component(AoADataComponents.BOW_STATS.get(), new BowStats(damage, drawSpeedModifier));
    }
}
