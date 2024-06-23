package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.world.level.block.Block;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class AbyssPortalBlock extends PortalBlock {
    public AbyssPortalBlock(Properties properties) {
        super(properties, AoADimensions.ABYSS, ColourUtil.RGB(229, 0, 0));
    }

    @Override
    public Block getPortalFrame() {
        return AoABlocks.DARKSTONE.get();
    }
}
