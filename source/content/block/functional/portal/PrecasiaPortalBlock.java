package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class PrecasiaPortalBlock extends PortalBlock {
    public PrecasiaPortalBlock(Properties properties) {
        super(properties, AoADimensions.PRECASIA, ColourUtil.RGB(207, 221, 0), AoASounds.BLOCK_PRECASIA_PORTAL_AMBIENT);
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.PRECASIAN_STONE.stone();
    }
}
