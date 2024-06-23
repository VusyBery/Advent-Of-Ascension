package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.ToxicShotEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class ToxicShotRenderer extends ParticleProjectileRenderer<ToxicShotEntity> {
	public ToxicShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(ToxicShotEntity entity, float partialTicks) {
		for (int i = 0; i < 3; i++) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
					.colourOverride(0x336600)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.5f)
					.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
					.colourOverride(0x1E1D16)
					.spawnParticles(entity.level());
		}
	}
}