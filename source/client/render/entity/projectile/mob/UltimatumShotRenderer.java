package net.tslat.aoa3.client.render.entity.projectile.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.UltimatumShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class UltimatumShotRenderer extends ParticleProjectileRenderer<UltimatumShotEntity> {
	public UltimatumShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(UltimatumShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.scaleMod(0.25f)
				.colourOverride(ColourUtil.CYAN)
				.spawnParticles(entity.level());

		float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.colourOverride(colourMod, colourMod, colourMod, 1f)
				.spawnParticles(entity.level());

		colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().add(RandomUtil.randomValueBetween(-0.5d, 0.5d), RandomUtil.randomValueBetween(-0.5d, 0.5d), RandomUtil.randomValueBetween(-0.5d, 0.5d)))
				.scaleMod(0.5f)
				.colourOverride(colourMod, colourMod, 0, 1f)
				.spawnParticles(entity.level());
	}
}