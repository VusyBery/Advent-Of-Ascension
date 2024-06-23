package net.tslat.aoa3.client.render.entity.projectile.staff;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.staff.LyonicShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class LyonicShotRenderer extends ParticleProjectileRenderer<LyonicShotEntity> {
	public LyonicShotRenderer(final EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	protected void addParticles(LyonicShotEntity entity, float partialTicks) {
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.colourOverride(RandomUtil.getRandomSelection(ColourUtil.RGB(30, 29, 22), ColourUtil.RGB(204, 172, 0)))
				.spawnParticles(entity.level());
	}
}