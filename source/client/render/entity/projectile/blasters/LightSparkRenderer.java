package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.LightSparkEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class LightSparkRenderer extends ParticleProjectileRenderer<LightSparkEntity> {
	public LightSparkRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(LightSparkEntity entity, float partialTicks) {
		for (int i = 0; i < 3; i++) {
			
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(0, entity.level().random.nextFloat() * 0.7f + 0.3f, 0, 1f)
					.spawnParticles(entity.level());

		}

		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(3)
				.colourOverride(ColourUtil.WHITE)
				.spawnParticles(entity.level());
	}
}