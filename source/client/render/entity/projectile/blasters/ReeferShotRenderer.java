package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.ReeferShotEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class ReeferShotRenderer extends ParticleProjectileRenderer<ReeferShotEntity> {
	public ReeferShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(ReeferShotEntity entity, float partialTicks) {
		for (int i = 0; i < 3; i++) {
			if (entity.toggle1) {
				entity.yOffset1 += 0.12;

				if (entity.yOffset1 >= 3.0f)
					entity.toggle1 = false;
			}
			if (!entity.toggle1) {
				entity.yOffset1 -= 0.12;

				if (entity.yOffset1 <= -3.0f)
					entity.toggle1 = true;
			}

			if (entity.toggle2) {
				entity.yOffset2 += 0.12;

				if (entity.yOffset2 >= 3.0f)
					entity.toggle2 = false;
			}
			if (!entity.toggle2) {
				entity.yOffset2 -= 0.12;

				if (entity.yOffset2 <= -3.0f)
					entity.toggle2 = true;
			}

			for (int j = 0; j < 3; j++) {
				float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

				ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(0, entity.yOffset1, 0))
					.colourOverride(0, colourMod, colourMod, 1f)
					.spawnParticles(entity.level());
				ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(0, entity.yOffset2, 0))
					.colourOverride(entity.level().random.nextFloat() * 0.7f + 0.3f, 0, 0, 1f)
					.spawnParticles(entity.level());
			}
		}
	}
}