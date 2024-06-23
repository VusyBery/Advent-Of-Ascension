package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class DustopiaPortalBlock extends PortalBlock {
    public DustopiaPortalBlock(Properties properties) {
        super(properties, AoADimensions.DUSTOPIA, ColourUtil.BLACK);
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.DAWN_LEAVES.get();
    }
}
