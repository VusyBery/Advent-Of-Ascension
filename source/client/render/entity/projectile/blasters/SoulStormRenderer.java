package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.SoulStormEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class SoulStormRenderer extends ParticleProjectileRenderer<SoulStormEntity> {
	public SoulStormRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(SoulStormEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(6)
				.scaleMod(0.25f)
				.colourOverride(ColourUtil.CYAN)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(6)
				.scaleMod(0.25f)
				.colourOverride(ColourUtil.BLUE)
				.spawnParticles(entity.level());
	}
}