package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.RainbowShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class RainbowShotRenderer extends ParticleProjectileRenderer<RainbowShotEntity> {
	public RainbowShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(RainbowShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(0, 1.5f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(ColourUtil.RED)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(0, 1f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(0xDF9900)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(0, 0.5f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(ColourUtil.YELLOW)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(ColourUtil.GREEN)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 0.5f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(ColourUtil.CYAN)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 1f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(ColourUtil.BLUE)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 1.5f, 0))
				.spawnNTimes(3)
				.lifespan(Mth.ceil(20 / RandomUtil.randomValueBetween(0.2f, 1)))
				.colourOverride(0xC140D7)
				.spawnParticles(entity.level());
	}
}