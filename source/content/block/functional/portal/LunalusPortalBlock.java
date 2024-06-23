package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class LunalusPortalBlock extends PortalBlock {
    public LunalusPortalBlock(Properties properties) {
        super(properties, AoADimensions.LUNALUS, ColourUtil.RGB(255, 226, 251));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.TWINKLESTONE.get();
    }
}
