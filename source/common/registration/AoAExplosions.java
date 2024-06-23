package net.tslat.aoa3.common.registration;

import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.library.object.AllDirections;
import net.tslat.aoa3.library.object.explosion.ExplosionInfo;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.effectslib.networking.packet.TELParticlePacket;

public final class AoAExplosions {
	public static final ExplosionInfo BOMB_CARRIER_DYNAMITE = new ExplosionInfo()
			.radius(3f)
			.baseDamage(8)
			.penetration(8.5f)
			.blocksDropChance(0.3f);
	public static final ExplosionInfo KING_BAMBAMBAM_DISCHARGE = new ExplosionInfo()
			.radius(5)
			.baseDamage(40)
			.damageMod((explosion, target) -> PlayerUtil.getPlayerOrOwnerIfApplicable(target) != null ? 1 : 0f)
			.onHit((explosion, target) -> target.igniteForSeconds(target.getRemainingFireTicks() / 20 + 10));
	public static final ExplosionInfo LITTLE_BAM_OVERLOAD = new ExplosionInfo()
			.radius(6)
			.baseDamage(20)
			.penetration(30)
			.baseKnockbackStrength(1.25f)
			.onHit((explosion, entity) -> entity.igniteForSeconds(3))
			.onBlockHit((explosion, state, pos) -> {
				if (explosion.level.random.nextInt(3) == 0 && explosion.level.getBlockState(pos.below()).isSolidRender(explosion.level, pos.below()))
					explosion.level.setBlock(pos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
			})
			.particles((explosion, tick) -> {
				Vec3 pos = explosion.center();
				TELParticlePacket packet = new TELParticlePacket(ParticleBuilder.forPosition(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y, pos.z));

				for (AllDirections direction : AllDirections.values()) {
					Vec3i angle = direction.angle();

					packet.particle(ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), pos)
							.lifespan(Mth.ceil(5 * (explosion.rand().nextFloat() * 0.8f + 0.2f)))
							.colourOverride((explosion.rand().nextFloat() * 0.7f + 0.3f) * 124 / 255f, 0, 0, 1f)
							.velocity(angle.getX() * 10, angle.getY() * 10, angle.getZ() * 10));
				}

				packet.sendToAllNearbyPlayers((ServerLevel)explosion.level, pos, 64);
			});
	public static final ExplosionInfo RPG = new ExplosionInfo()
			.radius(4)
			.penetration(20)
			.blocksDropChance(0.55f)
			.baseDamage(12)
			.baseKnockbackStrength(2.5f);
	public static final ExplosionInfo GRENADE = new ExplosionInfo()
			.radius(3f)
			.penetration(7)
			.baseDamage(5)
			.blocksDropChance(0.1f);
	public static final ExplosionInfo NETHENGEIC_WITHER_FIREBALL = new ExplosionInfo()
			.radius(2)
			.penetration(50)
			.baseDamage(5)
			.onBlockHit((explosion, state, explodePos) -> {
				if (explosion.rand().oneInNChance(3) && explosion.level.getBlockState(explodePos.below()).isSolidRender(explosion.level, explodePos.below()))
					explosion.level.setBlock(explodePos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
			});
	public static final ExplosionInfo STICKY_FIREBALL = new ExplosionInfo()
			.radius(2)
			.baseDamage(1)
			.damageMod((explosion, target) -> (target.level().getDifficulty().getId() + 1) * 5f);
	public static final ExplosionInfo EXPLOSIVE_BOW = new ExplosionInfo()
			.radius(2.5f)
			.baseDamage(4)
			.penetration(5)
			.knockbackMod((explosion, entity) -> 1.2f)
			.blocksDropChance(0.1f);
}
