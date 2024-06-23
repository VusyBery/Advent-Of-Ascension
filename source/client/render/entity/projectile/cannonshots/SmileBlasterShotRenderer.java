package net.tslat.aoa3.client.render.entity.projectile.cannonshots;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tslat.aoa3.client.render.entity.projectile.TexturedProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.cannon.SmileBlasterEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class SmileBlasterShotRenderer extends TexturedProjectileRenderer<SmileBlasterEntity> {
	private int counter = 12;
	private boolean toggle;

	public SmileBlasterShotRenderer(final EntityRendererProvider.Context manager, final ResourceLocation textureResource) {
		super(manager, textureResource);
	}

	@Override
	public void render(SmileBlasterEntity entity, float entityYaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, packedLight);

		for (int i = 0; i < 8; i++) {
			counter--;

			if (counter == 0) {
				toggle = !toggle;
				counter = 12;
			}

			if (toggle) {
				float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

				ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
						.colourOverride(colourMod, colourMod, 0, 1f)
						.spawnParticles(entity.level());
			}
			else {
				ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.colourOverride(0, 0, 0, 1f)
				.spawnParticles(entity.level());
			}
		}
	}
}