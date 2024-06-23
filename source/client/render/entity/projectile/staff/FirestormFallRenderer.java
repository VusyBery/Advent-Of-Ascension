package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.FirestormFallEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class FirestormFallRenderer extends ParticleProjectileRenderer<FirestormFallEntity> {
	public FirestormFallRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(FirestormFallEntity entity, float partialTicks) {
		for (int i = 0; i < 8; i++) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(entity.level().random.nextFloat() * 0.7f + 0.3f, 0, 0, 1f)
					.spawnParticles(entity.level());
		}
	}
}