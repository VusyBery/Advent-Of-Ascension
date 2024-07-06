package net.tslat.aoa3.content.entity.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class GyrocopterEntity extends Entity {
    /*public GyrocopterEntity(Player player) {
        this(AoAMiscEntities.GYROCOPTER.get(), player.level());

        double offsetX = -Mth.sin(player.getYRot() * (float)Math.PI / 180f);
        double offsetZ = Mth.cos(player.getYRot() * (float)Math.PI / 180f);

        moveTo(player.getX() + offsetX * 1.5, player.getY(), player.getZ() + offsetZ * 1.5, player.getYRot(), 0);
    }*/

    public GyrocopterEntity(EntityType<? extends Entity> entityType, Level world) {
        super(entityType, world);

        setSilent(true);
    }

    @Override
    public boolean canChangeDimensions(Level from, Level to) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    public void tick() {
        super.tick();

        /*if (tickCount >= 200) {
            if (!level.isClientSide)
                level.addFreshEntity(new GyroEntity(this));

            discard();

            return;
        }*/

        move(MoverType.SELF, new Vec3(0, 0.1d, 0));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(Tags.DamageTypes.IS_TECHNICAL);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {}
}