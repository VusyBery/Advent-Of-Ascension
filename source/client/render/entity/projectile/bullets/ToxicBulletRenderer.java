package net.tslat.aoa3.client.render.entity.projectile.bullets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tslat.aoa3.client.render.entity.projectile.TexturedProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.gun.ToxicBulletEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class ToxicBulletRenderer extends TexturedProjectileRenderer<ToxicBulletEntity> {
	public ToxicBulletRenderer(final EntityRendererProvider.Context manager, final ResourceLocation textureResource) {
		super(manager, textureResource);
	}

	@Override
	public void render(ToxicBulletEntity entity, float entityYaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, packedLight);

		for (int i = 0; i < 8; i++) {
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(0, entity.level().random.nextFloat() * 0.7f + 0.3f, 0, 1f)
					.spawnParticles(entity.level());
		}
	}
}