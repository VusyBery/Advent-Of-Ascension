package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class HavenPortalBlock extends PortalBlock {
    public HavenPortalBlock(Properties properties) {
        super(properties, AoADimensions.HAVEN, ColourUtil.RGB(0, 229, 237));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.TWINKLESTONE.get();
    }
}
