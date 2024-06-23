package net.tslat.aoa3.content.block.functional.portal;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import net.tslat.aoa3.util.ColourUtil;

public class NetherPortalBlock extends PortalBlock {
    public NetherPortalBlock(Properties properties) {
        super(properties, AoADimensions.NETHER, ColourUtil.RGB(193, 64, 215), () -> SoundEvents.PORTAL_AMBIENT);
    }

    @Override
    public Block getPortalFrame() {
        return Blocks.GLOWSTONE;
    }
}
