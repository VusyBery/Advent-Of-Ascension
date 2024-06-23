package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class DeeplandsPortalBlock extends PortalBlock {
    public DeeplandsPortalBlock(Properties properties) {
        super(properties, AoADimensions.DEEPLANDS, ColourUtil.RGB(181, 181, 181));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.DENSE_STONE.stone();
    }
}
