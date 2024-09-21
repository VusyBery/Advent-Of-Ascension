package net.tslat.aoa3.content.world.gen.feature.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BlockTowerFeature extends Feature<BlockTowerFeature.Configuration> {
    public BlockTowerFeature(Codec<BlockTowerFeature.Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final BlockPos basePos = context.origin();
        final Configuration config = context.config();
        final int baseRadius = config.baseRadius.sample(random);
        final int tipRadius = config.tipRadius.sample(random);
        final int height = config.height.sample(random);
        final float pitting = config.pitting.sample(random);
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < height; i++) {
            final BlockPos columnPos = basePos.offset(0, i, 0);
            final float width = Mth.lerp(i / (float)height, baseRadius, tipRadius);
            float xRadius = random.nextFloat() * width;
            float yRadius = random.nextFloat() * width;
            float zRadius = random.nextFloat() * width;
            final float radius = (xRadius + yRadius + zRadius) / 3f + 0.5F;
            xRadius = Mth.floor(xRadius);
            yRadius = Mth.floor(yRadius);
            zRadius = Mth.floor(zRadius);

            for (int x = -(int)xRadius; x <= xRadius; x++) {
                for (int y = -(int)yRadius; y <= yRadius; y++) {
                    for (int z = -(int)zRadius; z <= zRadius; z++) {
                        double dist = columnPos.distSqr(pos.setWithOffset(columnPos, x, y, z));

                        if (dist <= radius * radius) {
                            final boolean canDoPitting = pitting > 0 && radius > 2 && (Mth.abs(x) == Mth.abs(xRadius) || Mth.abs(z) == Mth.abs(zRadius));

                            if (!canDoPitting || random.nextFloat() > pitting)
                                level.setBlock(pos, config.blockState.getState(random, pos), Block.UPDATE_ALL);

                            if (i == 0) {
                                while (level.getBlockState(pos.move(Direction.DOWN)).isAir() && pos.getY() > level.getMinBuildHeight()) {
                                    if (!canDoPitting || random.nextFloat() > pitting)
                                        level.setBlock(pos, config.blockState.getState(random, pos), Block.UPDATE_ALL);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public record Configuration(IntProvider baseRadius, IntProvider tipRadius, IntProvider height, FloatProvider pitting, BlockStateProvider blockState) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                IntProvider.POSITIVE_CODEC.fieldOf("base_radius").forGetter(Configuration::baseRadius),
                IntProvider.POSITIVE_CODEC.fieldOf("tip_radius").forGetter(Configuration::tipRadius),
                IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(Configuration::height),
                FloatProvider.codec(0, 1).fieldOf("pitting").forGetter(Configuration::pitting),
                BlockStateProvider.CODEC.fieldOf("block").forGetter(Configuration::blockState)
        ).apply(builder, Configuration::new));
    }
}
