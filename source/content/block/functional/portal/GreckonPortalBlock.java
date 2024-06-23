package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class GreckonPortalBlock extends PortalBlock {
    public GreckonPortalBlock(Properties properties) {
        super(properties, AoADimensions.GRECKON, ColourUtil.RGB(130, 178, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.HAUNTED_LEAVES.get();
    }
}
