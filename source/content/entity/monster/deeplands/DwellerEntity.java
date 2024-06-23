package net.tslat.aoa3.content.entity.monster.deeplands;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;

public class DwellerEntity extends AoAMeleeMob<DwellerEntity> {
    public DwellerEntity(EntityType<? extends DwellerEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public float getEyeHeightAccess(Pose pose) {
        return 2.125f;
    }

}
