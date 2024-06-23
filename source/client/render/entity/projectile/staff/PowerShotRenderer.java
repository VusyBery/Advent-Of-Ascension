package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.PowerShotEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class PowerShotRenderer extends ParticleProjectileRenderer<PowerShotEntity> {
	public PowerShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(PowerShotEntity entity, float partialTicks) {
		for (int i = 0; i < 8; i++) {
			float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(colourMod, colourMod, 0, 1f)
					.spawnParticles(entity.level());
		}
	}
}