package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class LboreanPortalBlock extends PortalBlock {
    public LboreanPortalBlock(Properties properties) {
        super(properties, AoADimensions.LBOREAN, ColourUtil.RGB(0, 173, 216));
    }

    @Override
    public Block getPortalFrame() {
        return Blocks.BUBBLE_CORAL_BLOCK;
    }

    @Override
    public void createPortalBaseAndDecorations(Level level, BlockPos pos, Direction.Axis direction) {
        final BlockState border = getPortalFrame().defaultBlockState();
        final BlockState air = Blocks.AIR.defaultBlockState();
        final Vec3 centerPos = Vec3.atBottomCenterOf(pos.above(4));

        for (int x = -4; x <= 4; x++) {
            for (int y = 7; y >= 0; y--) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos placePos = pos.offset(x, y, z);
                    double dist = Vec3.atCenterOf(placePos).distanceToSqr(centerPos);

                    if (dist <= 4 * 4 && level.isInWorldBounds(placePos)) {
                        BlockState state = level.getBlockState(placePos);

                        if (!state.is(BlockTags.PORTALS) && !state.is(border.getBlock()))
                            level.setBlockAndUpdate(placePos, dist <= 3 * 3 ? air : border);
                    }
                }
            }
        }
    }
}
