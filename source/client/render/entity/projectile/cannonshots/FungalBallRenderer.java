package net.tslat.aoa3.client.render.entity.projectile.cannonshots;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tslat.aoa3.client.render.entity.projectile.TexturedProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.cannon.FungalBallEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class FungalBallRenderer extends TexturedProjectileRenderer<FungalBallEntity> {
	public FungalBallRenderer(final EntityRendererProvider.Context manager, final ResourceLocation textureResource) {
		super(manager, textureResource);
	}

	@Override
	public void render(FungalBallEntity entity, float entityYaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, packedLight);

		for (int i = 0; i < 8; i++) {
			switch (RandomUtil.randomNumberUpTo(4)) {
				case 0 -> {
					
			ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
					.colourOverride(0, entity.level().random.nextFloat() * 0.7f + 0.3f, 0, 1f)
					.spawnParticles(entity.level());
				}
				case 1 -> {
					ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
							.colourOverride(ColourUtil.YELLOW)
							.spawnParticles(entity.level());
				}
				case 2 -> {
					ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
							.colourOverride(ColourUtil.BLUE)
							.spawnParticles(entity.level());
				}
				case 3 -> {
					ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
							.colourOverride(ColourUtil.RGB(193, 64, 215))
							.spawnParticles(entity.level());
				}
			}
		}
	}
}