package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class LelyetiaPortalBlock extends PortalBlock {
    public LelyetiaPortalBlock(Properties properties) {
        super(properties, AoADimensions.LELYETIA, ColourUtil.RGB(221, 103, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.LELYETIAN_BRICKS.stone();
    }
}
