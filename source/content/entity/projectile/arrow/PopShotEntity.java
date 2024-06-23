package net.tslat.aoa3.content.entity.projectile.arrow;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.entity.AoAProjectiles;
import net.tslat.aoa3.common.registration.item.AoAItems;

public class PopShotEntity extends CustomArrowEntity {
	public final boolean isExplosive;

	public PopShotEntity(EntityType<? extends Arrow> entityType, Level world) {
		super(entityType, world);

		this.isExplosive = true;
	}

	public PopShotEntity(Level world) {
		super(AoAProjectiles.POP_SHOT.get(), world);

		this.isExplosive = true;
	}

	public PopShotEntity(Level world, double x, double y, double z) {
		super(AoAProjectiles.POP_SHOT.get(), world);

		this.isExplosive = true;

		setPos(x, y, z);
	}

	public PopShotEntity(Level world, ItemStack weaponStack, LivingEntity shooter, double baseDamage, boolean isExplosive) {
		super(AoAProjectiles.POP_SHOT.get(), world);

		setOwner(shooter);
		setBaseDamage(baseDamage);
		setPos(shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());

		this.firedFromWeapon = weaponStack;
		this.isExplosive = isExplosive;
	}

	protected void doPostHurtEffects(LivingEntity target) {}

	protected ItemStack getPickupItem() {
		return new ItemStack(AoAItems.POP_SHOT.get());
	}
}
