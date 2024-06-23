package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.MoonlightFallEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class MoonlightFallRenderer extends ParticleProjectileRenderer<MoonlightFallEntity> {
	public MoonlightFallRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(MoonlightFallEntity entity, float partialTicks) {
		for (int i = 0; i < 8; i++) {
			float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(0, colourMod, colourMod, 1f)
					.spawnParticles(entity.level());
		}
	}
}