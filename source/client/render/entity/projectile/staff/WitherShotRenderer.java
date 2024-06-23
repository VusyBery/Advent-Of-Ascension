package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.WitherShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class WitherShotRenderer extends ParticleProjectileRenderer<WitherShotEntity> {
	public WitherShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(WitherShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.ignoreDistanceAndLimits()
				.spawnNTimes(8)
				.colourOverride(ColourUtil.BLACK)
				.spawnParticles(entity.level());
	}
}
