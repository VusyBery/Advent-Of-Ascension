package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.blaster.VortexBlastEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class VortexBlastRenderer extends ParticleProjectileRenderer<VortexBlastEntity> {
	public VortexBlastRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(VortexBlastEntity entity, float partialTicks) {
		if (RandomUtil.fiftyFifty()) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.spawnNTimes(3)
					.scaleMod(0.25f)
					.colourOverride(ColourUtil.YELLOW)
					.spawnParticles(entity.level());
		}
		else {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.spawnNTimes(3)
					.colourOverride(ColourUtil.WHITE)
					.spawnParticles(entity.level());
		}
	}
}