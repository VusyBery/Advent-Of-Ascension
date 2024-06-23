package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class CelevePortalBlock extends PortalBlock {
    public CelevePortalBlock(Properties properties) {
        super(properties, AoADimensions.CELEVE, ColourUtil.RGB(247, 239, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.WHITE_CELEVUS_LEAVES.get();
    }
}
