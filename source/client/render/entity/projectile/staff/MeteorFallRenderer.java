package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.MeteorFallEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class MeteorFallRenderer extends ParticleProjectileRenderer<MeteorFallEntity> {
	public MeteorFallRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(MeteorFallEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.ignoreDistanceAndLimits()
				.spawnNTimes(3)
				.colourOverride(ColourUtil.RED)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 0.3d, 0))
				.ignoreDistanceAndLimits()
				.spawnNTimes(3)
				.colourOverride(0xDF9900)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 0.6d, 0))
				.ignoreDistanceAndLimits()
				.spawnNTimes(3)
				.colourOverride(ColourUtil.YELLOW)
				.spawnParticles(entity.level());
	}
}