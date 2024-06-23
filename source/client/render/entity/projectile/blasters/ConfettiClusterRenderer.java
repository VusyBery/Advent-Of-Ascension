package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.ConfettiClusterEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class ConfettiClusterRenderer extends ParticleProjectileRenderer<ConfettiClusterEntity> {
	public ConfettiClusterRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(ConfettiClusterEntity entity, float partialTicks) {
		Vec3 motion = entity.getDeltaMovement();

		for (int i = 0; i < 3; i++) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion)
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.add(0.05f, 0, 0))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(0.05f, 0, 0))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.add(0, 0, 0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(0, 0, 0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(0.05f, 0, 0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(-0.05f, 0, -0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(-0.05f, 0, 0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.scaleMod(0.15f)
					.lifespan(Mth.ceil(10 / RandomUtil.randomValueBetween(0.2f, 1)))
					.power(motion.subtract(0.05f, 0, -0.05f))
					.colourOverride((float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), (float)RandomUtil.randomGaussianValue(), 1f)
					.spawnParticles(entity.level());
		}
	}
}