package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class CandylandPortalBlock extends PortalBlock {
    public CandylandPortalBlock(Properties properties) {
        super(properties, AoADimensions.CANDYLAND, ColourUtil.RGB(255, 232, 232));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.WHITE_CANDY.get();
    }
}
