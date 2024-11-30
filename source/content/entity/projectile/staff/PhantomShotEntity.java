package net.tslat.aoa3.content.entity.projectile.staff;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.tslat.aoa3.common.registration.entity.AoAProjectiles;
import net.tslat.aoa3.content.item.EnergyProjectileWeapon;
import net.tslat.aoa3.util.EntityUtil;

import java.util.UUID;

public class PhantomShotEntity extends BaseEnergyShot {
	private UUID lastHit = null;

	public PhantomShotEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
		super(entityType, world);
	}
	
	public PhantomShotEntity(Level world) {
		super(AoAProjectiles.PHANTOM_SHOT.get(), world);
	}

	public PhantomShotEntity(LivingEntity shooter, EnergyProjectileWeapon weapon, int maxAge) {
		super(AoAProjectiles.PHANTOM_SHOT.get(), shooter, weapon, maxAge);
	}

	public PhantomShotEntity(Level world, double x, double y, double z) {
		super(AoAProjectiles.PHANTOM_SHOT.get(), world, x, y, z);
	}

	@Override
	protected void onHit(HitResult result) {
		if (!level().isClientSide) {
			if (weapon != null) {
				if (result.getType() == HitResult.Type.BLOCK) {
					Entity shooter = getOwner();

					if (shooter instanceof LivingEntity)
						weapon.doBlockImpact(this, result.getLocation(), (LivingEntity)shooter);

					discard();
				}
				else if (result instanceof EntityHitResult entityResult) {
					Entity realTarget = EntityUtil.getPartOrPartOwner(entityResult.getEntity());

					if (realTarget.getUUID().equals(this.lastHit))
						return;

					Entity shooter = getOwner();
					this.lastHit = realTarget.getUUID();

					if (shooter instanceof LivingEntity)
						this.weapon.doEntityImpact(this, entityResult.getEntity(), (LivingEntity)shooter);
				}
			}
		}
	}
}
