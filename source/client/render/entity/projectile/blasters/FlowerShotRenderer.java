package net.tslat.aoa3.client.render.entity.projectile.blasters;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.tslat.aoa3.client.render.entity.projectile.ParticleProjectileRenderer;
import net.tslat.aoa3.common.particletype.CustomisableParticleType;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.object.entity.projectile.blaster.FlowerShotEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.aoa3.util.RandomUtil;

public class FlowerShotRenderer extends ParticleProjectileRenderer<FlowerShotEntity> {
	public FlowerShotRenderer(final EntityRendererManager manager) {
		super(manager);
	}

	@Override
	protected void addParticles(FlowerShotEntity entity, float partialTicks) {
		if (RandomUtil.fiftyFifty()) {
			for (int i = 0; i < 3; i++) {
				entity.level.addParticle(new CustomisableParticleType.Data(AoAParticleTypes.SPARKLER.get(), 1, 3, ColourUtil.GREEN), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
			}
		}
		else {
			for (int i = 0; i < 3; i++) {
				entity.level.addParticle(new CustomisableParticleType.Data(AoAParticleTypes.SPARKLER.get(), 1, 3, ColourUtil.YELLOW), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
			}
		}
	}
}