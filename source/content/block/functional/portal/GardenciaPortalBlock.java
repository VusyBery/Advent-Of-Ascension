package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class GardenciaPortalBlock extends PortalBlock {
    public GardenciaPortalBlock(Properties properties) {
        super(properties, AoADimensions.GARDENCIA, ColourUtil.RGB(255, 0, 114));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.GIANT_PLANT_STEM.get();
    }
}
