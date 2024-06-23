package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class CrysteviaPortalBlock extends PortalBlock {
    public CrysteviaPortalBlock(Properties properties) {
        super(properties, AoADimensions.CRYSTEVIA, ColourUtil.RGB(194, 73, 255));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.GREEN_CRYSTAL_BLOCK.get();
    }
}
