package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class IrominePortalBlock extends PortalBlock {
    public IrominePortalBlock(Properties properties) {
        super(properties, AoADimensions.IROMINE, ColourUtil.RGB(232, 208, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.IRO_STRIPED_BRICKS.stone();
    }
}
