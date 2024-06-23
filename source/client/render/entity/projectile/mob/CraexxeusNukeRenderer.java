package net.tslat.aoa3.client.render.entity.projectile.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.mob.CraexxeusNukeEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class CraexxeusNukeRenderer extends ParticleProjectileRenderer<CraexxeusNukeEntity> {
	public CraexxeusNukeRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(CraexxeusNukeEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_SWIRL.get(), entity.position().add(0, 0.25f, 0))
				.colourOverride(ColourUtil.YELLOW)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_SWIRL.get(), entity.position())
				.colourOverride(ColourUtil.CYAN)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_SWIRL.get(), entity.position().subtract(0, 0.25f, 0))
				.colourOverride(ColourUtil.YELLOW)
				.spawnParticles(entity.level());
	}
}