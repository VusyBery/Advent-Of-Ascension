package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class BarathosPortalBlock extends PortalBlock {
    public BarathosPortalBlock(Properties properties) {
        super(properties, AoADimensions.BARATHOS, ColourUtil.RGB(239, 137, 119));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.BARON_STONE_BRICKS.stone();
    }
}
