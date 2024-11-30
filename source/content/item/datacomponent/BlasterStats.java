package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.item.weapon.blaster.BaseBlaster;

public record BlasterStats(float damage, int ticksBetweenShots, float spiritCost, int chargeUpTicks) {
    public static final Codec<BlasterStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("damage").forGetter(BlasterStats::damage),
            Codec.INT.fieldOf("ticks_between_shots").forGetter(BlasterStats::ticksBetweenShots),
            Codec.FLOAT.fieldOf("spirit_cost").forGetter(BlasterStats::spiritCost),
            Codec.INT.fieldOf("charge_up_ticks").forGetter(BlasterStats::chargeUpTicks)
    ).apply(builder, BlasterStats::new));
    public static final StreamCodec<FriendlyByteBuf, BlasterStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BlasterStats::damage,
            ByteBufCodecs.VAR_INT, BlasterStats::ticksBetweenShots,
            ByteBufCodecs.FLOAT, BlasterStats::spiritCost,
            ByteBufCodecs.VAR_INT, BlasterStats::chargeUpTicks,
            BlasterStats::new);

    public static Item.Properties of(float damage, int ticksBetweenShots, float spiritCost, int chargeUpTicks) {
        return new Item.Properties()
                .component(AoADataComponents.BLASTER_STATS.get(), new BlasterStats(damage, ticksBetweenShots, spiritCost, chargeUpTicks))
                .attributes(BaseBlaster.createBlasterAttributeModifiers(1.2f));
    }
}
