package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class ShyrelandsPortalBlock extends PortalBlock {
    public ShyrelandsPortalBlock(Properties properties) {
        super(properties, AoADimensions.SHYRELANDS, ColourUtil.YELLOW);
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.YELLOW_SHYRE_BRICKS.stone();
    }
}
