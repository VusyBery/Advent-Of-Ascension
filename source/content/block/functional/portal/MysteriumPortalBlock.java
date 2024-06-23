package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class MysteriumPortalBlock extends PortalBlock {
    public MysteriumPortalBlock(Properties properties) {
        super(properties, AoADimensions.MYSTERIUM, ColourUtil.RGB(107, 0, 82));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.BLUE_MUSHROOM_BLOCK.get();
    }
}
