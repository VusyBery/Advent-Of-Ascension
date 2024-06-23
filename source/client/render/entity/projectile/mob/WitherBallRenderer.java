package net.tslat.aoa3.client.render.entity.projectile.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.mob.WitherBallEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class WitherBallRenderer extends ParticleProjectileRenderer<WitherBallEntity> {
	public WitherBallRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(WitherBallEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.colourOverride(0, 0, 0, 1f)
				.spawnParticles(entity.level());
	}
}