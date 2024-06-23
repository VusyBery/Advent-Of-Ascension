package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.PolymorphShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class PolymorphShotRenderer extends ParticleProjectileRenderer<PolymorphShotEntity> {
	public PolymorphShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(PolymorphShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(8)
				.colourOverride(ColourUtil.WHITE)
				.spawnParticles(entity.level());
	}
}