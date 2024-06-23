package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class VoxPondsPortalBlock extends PortalBlock {
    public VoxPondsPortalBlock(Properties properties) {
        super(properties, AoADimensions.VOX_PONDS, ColourUtil.RGB(90, 104, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.DEGRADED_STEEL.get();
    }
}
