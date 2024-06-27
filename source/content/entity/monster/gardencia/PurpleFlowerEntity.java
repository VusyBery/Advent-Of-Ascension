/*
package net.tslat.aoa3.content.entity.mob.gardencia;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import net.tslat.aoa3.content.entity.boss.VinocorneEntity;

public class PurpleFlowerEntity extends AoAMeleeMob<PurpleFlowerEntity> {
	public PurpleFlowerEntity(VinocorneEntity vinocorne) {
		this(AoAMobs.PURPLE_FLOWER.get(), vinocorne.level);

		moveTo(vinocorne.getX(), vinocorne.getY(), vinocorne.getZ(), random.nextFloat() * 360, 0);
	}

	public PurpleFlowerEntity(EntityType<? extends PurpleFlowerEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public float getEyeHeightAccess(Pose pose) {
		return 1.5f;
	}

}
*/