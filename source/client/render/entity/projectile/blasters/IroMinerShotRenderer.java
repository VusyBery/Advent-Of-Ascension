package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.IroMinerShotEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class IroMinerShotRenderer extends ParticleProjectileRenderer<IroMinerShotEntity> {
	public IroMinerShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(IroMinerShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(3)
				.colourOverride(0xCCAC00)
				.spawnParticles(entity.level());
	}
}