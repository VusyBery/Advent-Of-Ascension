package net.tslat.aoa3.content.world.gen.blockpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.tslat.aoa3.common.registration.worldgen.AoABlockPredicateTypes;

public record CheckYRange(IntProvider minY, IntProvider maxY) implements BlockPredicate {
    public static MapCodec<CheckYRange> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            IntProvider.CODEC.fieldOf("min_inclusive").forGetter(CheckYRange::minY),
            IntProvider.CODEC.fieldOf("max_exclusive").forGetter(CheckYRange::maxY)
    ).apply(builder, CheckYRange::new));

    public static CheckYRange forRange(int minInclusive, int maxExclusive) {
        return forRange(ConstantInt.of(minInclusive), ConstantInt.of(maxExclusive));
    }

    public static CheckYRange forRange(IntProvider minInclusive, IntProvider maxExclusive) {
        return new CheckYRange(minInclusive, maxExclusive);
    }

    @Override
    public BlockPredicateType<?> type() {
        return AoABlockPredicateTypes.CHECK_Y_RANGE.get();
    }

    @Override
    public boolean test(WorldGenLevel level, BlockPos pos) {
        return pos.getY() >= this.minY.sample(level.getRandom()) && pos.getY() < this.maxY.sample(level.getRandom());
    }
}
