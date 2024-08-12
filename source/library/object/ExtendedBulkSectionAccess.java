package net.tslat.aoa3.library.object;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ExtendedBulkSectionAccess extends BulkSectionAccess {
    public ExtendedBulkSectionAccess(LevelAccessor level) {
        super(level);
    }

    public FluidState getFluidState(BlockPos pos) {
        LevelChunkSection chunkSection = getSection(pos);

        if (chunkSection == null)
            return Fluids.EMPTY.defaultFluidState();

        return chunkSection.getFluidState(SectionPos.sectionRelative(pos.getX()), SectionPos.sectionRelative(pos.getY()), SectionPos.sectionRelative(pos.getZ()));
    }
}
