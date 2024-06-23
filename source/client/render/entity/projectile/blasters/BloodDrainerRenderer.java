package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.BloodDrainerEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class BloodDrainerRenderer extends ParticleProjectileRenderer<BloodDrainerEntity> {
	public BloodDrainerRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(BloodDrainerEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_SWIRL.get(), entity.position())
				.spawnNTimes(3)
				.colourOverride(ColourUtil.RED)
				.spawnParticles(entity.level());
	}
}
