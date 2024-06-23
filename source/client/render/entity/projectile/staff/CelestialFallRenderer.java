package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.CelestialFallEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class CelestialFallRenderer extends ParticleProjectileRenderer<CelestialFallEntity> {
	public CelestialFallRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(CelestialFallEntity entity, float partialTicks) {
		for (int i = 0; i < 8; i++) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
						.colourOverride((entity.level().random.nextFloat() * 0.8f + 0.2f), (entity.level().random.nextFloat() * 0.8f + 0.2f), 0, 1f)
						.spawnParticles(entity.level());
		}
	}
}