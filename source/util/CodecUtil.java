package net.tslat.aoa3.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.tslat.aoa3.library.object.TriFunction;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.BiFunction;

public final class CodecUtil {
    public static final StreamCodec<FriendlyByteBuf, OptionalDouble> STREAM_OPTIONAL_DOUBLE = StreamCodec.of((buf, value) -> {
        buf.writeBoolean(value.isPresent());

        if (value.isPresent())
            buf.writeDouble(value.getAsDouble());
    }, buf -> buf.readBoolean() ? OptionalDouble.of(buf.readDouble()) : OptionalDouble.empty());
    public static final StreamCodec<FriendlyByteBuf, OptionalInt> STREAM_OPTIONAL_INT = StreamCodec.of((buf, value) -> {
        buf.writeBoolean(value.isPresent());

        if (value.isPresent())
            buf.writeInt(value.getAsInt());
    }, buf -> buf.readBoolean() ? OptionalInt.of(buf.readInt()) : OptionalInt.empty());

    public static final MapCodec<OptionalInt> OPTIONAL_INT =  Codec.INT.optionalFieldOf("value")
            .xmap(optional -> optional.map(
                    OptionalInt::of).orElseGet(OptionalInt::empty),
                    optionalInt -> optionalInt.isPresent() ? Optional.of(optionalInt.getAsInt()) : Optional.empty());
    public static final Codec<EnchantmentInstance> ENCHANTMENT_INSTANCE = RecordCodecBuilder.create(builder -> builder.group(
            Enchantment.CODEC.fieldOf("enchantment").forGetter(instance -> instance.enchantment),
            Codec.INT.fieldOf("level").forGetter(instance -> instance.level)
    ).apply(builder, EnchantmentInstance::new));

    public static <T, B extends FriendlyByteBuf> StreamCodec<B, NonNullList<T>> streamNonNullList(StreamCodec<B, T> elementCodec, T defaultElement) {
        return StreamCodec.of((buf, value) -> {
            buf.writeVarInt(value.size());

            for (T element : value) {
                elementCodec.encode(buf, element);
            }
        }, buf -> {
            NonNullList<T> list = NonNullList.withSize(buf.readVarInt(), defaultElement);

            list.replaceAll(element -> elementCodec.decode(buf));

            return list;
        });
    }

    public static <L, R, B extends ByteBuf> StreamCodec<B, Pair<L, R>> streamPair(StreamCodec<? super B, L> leftCodec, StreamCodec<? super B, R> rightCodec) {
        return streamPair(Pair::of, leftCodec, rightCodec);
    }

    public static <L, R, P extends Pair<L, R>, B extends ByteBuf> StreamCodec<B, P> streamPair(BiFunction<L, R, P> factory, StreamCodec<? super B, L> leftCodec, StreamCodec<? super B, R> rightCodec) {
        return StreamCodec.of((buf, value) -> {
            leftCodec.encode(buf, value.left());
            rightCodec.encode(buf, value.right());
        }, buf -> factory.apply(leftCodec.decode(buf), rightCodec.decode(buf)));
    }

    public static <A, B, C, T extends Triple<A, B, C>, BUF extends ByteBuf> StreamCodec<BUF, T> streamTriple(TriFunction<A, B, C, T> factory, StreamCodec<? super BUF, A> leftCodec, StreamCodec<? super BUF, B> middleCodec, StreamCodec<? super BUF, C> rightCodec) {
        return StreamCodec.of((buf, value) -> {
            leftCodec.encode(buf, value.getLeft());
            middleCodec.encode(buf, value.getMiddle());
            rightCodec.encode(buf, value.getRight());
        }, buf -> factory.apply(leftCodec.decode(buf), middleCodec.decode(buf), rightCodec.decode(buf)));
    }
}
