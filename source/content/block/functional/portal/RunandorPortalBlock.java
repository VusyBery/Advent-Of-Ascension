package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class RunandorPortalBlock extends PortalBlock {
    public RunandorPortalBlock(Properties properties) {
        super(properties, AoADimensions.RUNANDOR, ColourUtil.RGB(124, 255, 255));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.RUNIC_STONE.stone();
    }
}
