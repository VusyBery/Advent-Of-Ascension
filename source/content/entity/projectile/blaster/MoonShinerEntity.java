package net.tslat.aoa3.content.entity.projectile.blaster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.entity.AoAProjectiles;
import net.tslat.aoa3.content.entity.projectile.staff.BaseEnergyShot;
import net.tslat.aoa3.content.item.EnergyProjectileWeapon;

public class MoonShinerEntity extends BaseEnergyShot {
	public MoonShinerEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
		super(entityType, world);
	}

	public MoonShinerEntity(Level world) {
		super(AoAProjectiles.MOON_SHINER_SHOT.get(), world);
	}

	public MoonShinerEntity(LivingEntity shooter, EnergyProjectileWeapon weapon, int maxAge) {
		super(AoAProjectiles.MOON_SHINER_SHOT.get(), shooter, weapon, maxAge);
	}

	public MoonShinerEntity(Level world, double x, double y, double z) {
		super(AoAProjectiles.MOON_SHINER_SHOT.get(), world, x, y, z);
	}
}