package net.tslat.aoa3.content.entity.projectile.arrow;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.entity.AoAProjectiles;
import net.tslat.aoa3.content.item.ArrowFiringWeapon;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomArrowEntity extends Arrow {
	private boolean ignoreExplosions = false;

	public CustomArrowEntity(EntityType<? extends Arrow> type, Level world) {
		super(type, world);
	}

	public CustomArrowEntity(Level world, double x, double y, double z) {
		super(AoAProjectiles.ARROW.get(), world);

		setPos(x, y, z);
	}

	public CustomArrowEntity(Level world, ItemStack weaponStack, LivingEntity shooter, double baseDamage) {
		this(world, shooter.getX(), shooter.getEyeY() - 0.1f, shooter.getZ());

		setOwner(shooter);
		setBaseDamage(baseDamage);

		this.firedFromWeapon = weaponStack;
	}

	public static CustomArrowEntity fromArrow(AbstractArrow baseArrow, @Nullable ItemStack weaponStack, LivingEntity shooter, double baseDamage) {
		CustomArrowEntity arrow = new CustomArrowEntity(AoAProjectiles.ARROW.get(), baseArrow.level());

		arrow.setOwner(shooter);
		arrow.setCustomName(baseArrow.getCustomName());
		arrow.setCustomNameVisible(baseArrow.isCustomNameVisible());
		arrow.setBaseDamage(baseDamage);
		arrow.setCritArrow(baseArrow.isCritArrow());
		arrow.setPierceLevel(baseArrow.getPierceLevel());
		arrow.igniteForTicks(baseArrow.getRemainingFireTicks());
		arrow.setSoundEvent(arrow.getHitGroundSoundEvent());
		arrow.setPos(baseArrow.position().equals(Vec3.ZERO) ? shooter.getEyePosition().subtract(0, 0.1f, 0) : baseArrow.position());
		arrow.setDeltaMovement(baseArrow.getDeltaMovement());
		arrow.setXRot(baseArrow.getXRot());
		arrow.setYRot(baseArrow.getYRot());
		arrow.setPickupItemStack(baseArrow.getPickupItemStackOrigin());

		arrow.xRotO = baseArrow.xRotO;
		arrow.yRotO = baseArrow.yRotO;
		arrow.lastState = baseArrow.lastState;
		arrow.inGround = baseArrow.inGround;
		arrow.pickup = baseArrow.pickup;
		arrow.shakeTime = baseArrow.shakeTime;
		arrow.piercedAndKilledEntities = baseArrow.piercedAndKilledEntities;
		arrow.piercingIgnoreEntityIds = baseArrow.piercingIgnoreEntityIds;

		if (weaponStack != null) {
			arrow.firedFromWeapon = weaponStack.copy();

			if (arrow.level() instanceof ServerLevel serverlevel)
				EnchantmentHelper.onProjectileSpawned(serverlevel, weaponStack, arrow, p_348347_ -> arrow.firedFromWeapon = null);
		}

		return arrow;
	}

	protected static void duplicateArrowVelocity(AbstractArrow source, AbstractArrow target) {
	}

	@Override
	public void tick() {
		ItemStack weaponStack = getWeaponItem();

		if (weaponStack != null && weaponStack.getItem() instanceof ArrowFiringWeapon arrowFiringWeapon)
			arrowFiringWeapon.tickArrow(this, getOwner(), weaponStack);

		super.tick();
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return this.ignoreExplosions;
	}

	public void setIgnoreExplosions() {
		this.ignoreExplosions = true;
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult) {
		this.lastState = level().getBlockState(hitResult.getBlockPos());
		ItemStack weaponStack = getWeaponItem();

		if (weaponStack != null && weaponStack.getItem() instanceof ArrowFiringWeapon arrowFiringWeapon)
			arrowFiringWeapon.onBlockImpact(this, getOwner(), hitResult, weaponStack);

		this.lastState.onProjectileHit(level(), this.lastState, hitResult, this);

		Vec3 impactVelocity = hitResult.getLocation().subtract(getX(), getY(), getZ());

		setDeltaMovement(impactVelocity);

		if (level() instanceof ServerLevel serverlevel && weaponStack != null)
			hitBlockEnchantmentEffects(serverlevel, hitResult, weaponStack);

		Vec3 embedPos = position().subtract(impactVelocity.normalize().scale(0.05F));

		setPosRaw(embedPos.x, embedPos.y, embedPos.z);
		playSound(getHitGroundSoundEvent(), 1, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));

		this.inGround = true;
		this.shakeTime = 7;

		setCritArrow(false);
		setPierceLevel((byte)0);
		setSoundEvent(SoundEvents.ARROW_HIT);
		resetPiercedEntities();
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		Entity target = hitResult.getEntity();
		Entity owner = getOwner();
		float velocity = (float)this.getDeltaMovement().length();
		float damage = (float)getBaseDamage();
		ItemStack weaponStack = getWeaponItem();
		DamageSource damageSource = damageSources().arrow(this, owner != null ? owner : this);
		ArrowFiringWeapon arrowFiringWeapon = weaponStack != null && weaponStack.getItem() instanceof ArrowFiringWeapon weapon ? weapon : null;

		if (arrowFiringWeapon != null)
			damage = arrowFiringWeapon.getArrowDamage(this, owner, hitResult, weaponStack, damage, velocity, isCritArrow());

		if (weaponStack != null && level() instanceof ServerLevel level)
			damage = EnchantmentHelper.modifyDamage(level, weaponStack, target, damageSource, damage);

		damage = Math.max(damage, 0);

		if (getPierceLevel() > 0) {
			if (this.piercingIgnoreEntityIds == null)
				this.piercingIgnoreEntityIds = new IntOpenHashSet(5);

			if (this.piercedAndKilledEntities == null)
				this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);

			if (this.piercingIgnoreEntityIds.size() >= getPierceLevel() + 1) {
				discard();

				return;
			}

			this.piercingIgnoreEntityIds.add(target.getId());
		}

		boolean isEnderman = target.getType() == EntityType.ENDERMAN;
		int fireTicks = target.getRemainingFireTicks();

		if (isOnFire() && !isEnderman)
			target.igniteForSeconds(5);

		if (target.hurt(damageSource, damage)) {
			if (isEnderman)
				return;

			if (owner instanceof LivingEntity shooter)
				shooter.setLastHurtMob(target);

			if (arrowFiringWeapon != null)
				arrowFiringWeapon.onEntityImpact(this, owner, hitResult, weaponStack, velocity);

			if (target instanceof LivingEntity livingTarget) {
				if (!level().isClientSide && getPierceLevel() <= 0)
					livingTarget.setArrowCount(livingTarget.getArrowCount() + 1);

				doKnockback(livingTarget, damageSource);

				if (level() instanceof ServerLevel serverLevel)
					EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, livingTarget, damageSource, weaponStack);

				doPostHurtEffects(livingTarget);

				if (livingTarget != owner && livingTarget instanceof Player && owner instanceof ServerPlayer serverPlayer)
					serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0));

				if (!target.isAlive() && this.piercedAndKilledEntities != null)
					this.piercedAndKilledEntities.add(livingTarget);

				if (owner instanceof ServerPlayer serverPlayer && shotFromCrossbow()) {
					if (this.piercedAndKilledEntities != null) {
						CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, this.piercedAndKilledEntities);
					}
					else if (!target.isAlive()) {
						CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, List.of(target));
					}
				}
			}

			playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));

			if (getPierceLevel() <= 0)
				discard();
		}
		else {
			target.setRemainingFireTicks(fireTicks);
			deflect(ProjectileDeflection.REVERSE, target, owner, false);
			setDeltaMovement(getDeltaMovement().scale(0.2));

			if (!level().isClientSide && getDeltaMovement().lengthSqr() < 0.0000001) {
				if (pickup == Pickup.ALLOWED)
					spawnAtLocation(getPickupItem(), 0.1F);

				discard();
			}
		}
	}

	@Override
	public boolean shotFromCrossbow() {
		ItemStack weapon = getWeaponItem();

		return weapon != null && weapon.getItem() instanceof CrossbowItem;
	}
}
