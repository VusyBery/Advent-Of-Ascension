package net.tslat.aoa3.content.block.generation.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.Tags;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.content.world.event.AoAWorldEventManager;
import net.tslat.aoa3.content.world.event.BarathosSandstormEvent;

public class SandCatcher extends GenericPlantBlock {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public SandCatcher(Properties properties) {
        super(properties, state -> state.is(Tags.Blocks.SANDS), 12f, 16f);

        registerDefaultState(this.stateDefinition.any().setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(FILLED) && AoAWorldEventManager.getEventById(level, AdventOfAscension.id("barathos_sandstorm")) instanceof BarathosSandstormEvent ev && ev.isActive())
            level.setBlock(pos, state.setValue(FILLED, true), Block.UPDATE_ALL);
    }
}
