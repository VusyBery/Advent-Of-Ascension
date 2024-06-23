package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class CreeponiaPortalBlock extends PortalBlock {
    public CreeponiaPortalBlock(Properties properties) {
        super(properties, AoADimensions.CREEPONIA, ColourUtil.RGB(132, 188, 124));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.CREEPONIA_BRICKS.stone();
    }
}
