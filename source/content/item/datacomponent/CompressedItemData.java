package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record CompressedItemData(ItemStack compressedStack, int compressions) {
    public static final Codec<CompressedItemData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.STRICT_CODEC.fieldOf("compressed_stack").forGetter(CompressedItemData::compressedStack),
            Codec.intRange(1, 18).fieldOf("compressions").forGetter(CompressedItemData::compressions)
    ).apply(builder, CompressedItemData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompressedItemData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, CompressedItemData::compressedStack,
            ByteBufCodecs.VAR_INT, CompressedItemData::compressions,
            CompressedItemData::new);

    public CompressedItemData compress() {
        return new CompressedItemData(this.compressedStack, this.compressions + 1);
    }

    public CompressedItemData decompress() {
        return new CompressedItemData(this.compressedStack, this.compressions - 1);
    }
}
