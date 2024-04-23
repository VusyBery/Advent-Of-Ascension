package net.tslat.aoa3.content.entity.ai.movehelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;

public class MultiFluidSmoothGroundNavigation extends SmoothGroundNavigation {
    public MultiFluidSmoothGroundNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.onGround() || this.mob.isInFluidType((fluidType, height) -> canSwimInFluid(fluidType), true) || this.mob.isPassenger();
    }

    @Override
    public int getSurfaceY() {
        if (this.mob.isInFluidType((fluidType, height) -> canSwimInFluid(fluidType), true) && canFloat()) {
            final int basePos = this.mob.getBlockY();
            BlockPos.MutableBlockPos pos = BlockPos.containing(this.mob.getX(), basePos, this.mob.getZ()).mutable();
            BlockState state = this.level.getBlockState(pos);

            while (canSwimInFluid(state.getFluidState().getFluidType())) {
                state = this.level.getBlockState(pos.move(Direction.UP));

                if (pos.getY() - basePos > 16)
                    return basePos;
            }

            return pos.getY();
        }

        return Mth.floor(this.mob.getY() + 0.5);
    }

    protected boolean canSwimInFluid(FluidType fluidType) {
        return fluidType.canSwim(this.mob);
    }
}
