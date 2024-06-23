package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;

public record VulcaneStats(float damage) {
    public static final Codec<VulcaneStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("damage").forGetter(VulcaneStats::damage)
    ).apply(builder, VulcaneStats::new));
    public static final StreamCodec<FriendlyByteBuf, VulcaneStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, VulcaneStats::damage,
            VulcaneStats::new);

    public static Item.Properties of(float damage) {
        return new Item.Properties().component(AoADataComponents.VULCANE_STATS.get(), new VulcaneStats(damage));
    }
}
