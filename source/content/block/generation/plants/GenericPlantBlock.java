package net.tslat.aoa3.content.block.generation.plants;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class GenericPlantBlock extends BushBlock {
    private final VoxelShape shape;
    private final Predicate<BlockState> validSurface;

    public GenericPlantBlock(Properties properties, Predicate<BlockState> validSurface, float width, float height) {
        super(properties);

        float sidePadding = (16 - width) / 2f;
        this.shape = Block.box(sidePadding, 0, sidePadding, 16f - sidePadding, height, 16f - sidePadding);;
        this.validSurface = validSurface;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return null;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return this.validSurface.test(state);
    }
}
