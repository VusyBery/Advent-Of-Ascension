package net.tslat.aoa3.client.render.entity.projectile.bullets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tslat.aoa3.client.render.entity.projectile.TexturedProjectileRenderer;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.entity.projectile.gun.FireBulletEntity;
import net.tslat.aoa3.util.ColourUtil;
import net.tslat.effectslib.api.particle.ParticleBuilder;

public class FireBulletRenderer extends TexturedProjectileRenderer<FireBulletEntity> {
	public FireBulletRenderer(final EntityRendererProvider.Context manager, final ResourceLocation textureResource) {
		super(manager, textureResource);
	}

	@Override
	public void render(FireBulletEntity entity, float entityYaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrix, buffer, packedLight);

		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position())
				.spawnNTimes(8)
				.colourOverride(ColourUtil.RED)
				.spawnParticles(entity.level());
		ParticleBuilder.forPositions(AoAParticleTypes.GENERIC_DUST.get(), entity.position().subtract(0, 0.25f, 0))
				.spawnNTimes(8)
				.colourOverride(ColourUtil.YELLOW)
				.spawnParticles(entity.level());
	}
}