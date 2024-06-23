package net.tslat.aoa3.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.registration.AoAConfigs;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.content.block.blockentity.TrophyBlockEntity;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import net.tslat.smartbrainlib.util.RandomUtil;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity> {
	public TrophyRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	public void render(TrophyBlockEntity blockEntity, float partialTick, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		Entity entity = blockEntity.getCachedEntity();

		if (entity != null) {
			matrix.pushPose();
			matrix.translate(0.5f, 0, 0.5f);

			float scale = 0.53125F;
			float maxScale = Math.max(entity.getBbWidth(), entity.getBbHeight());
			double hover = (Mth.sin((float)blockEntity.hoverStep + partialTick) / 30d);
			blockEntity.hoverStep += hover * 0.01f;

			if (maxScale > 1.0D)
				scale /= maxScale;

			matrix.translate(0, -0.1, 0);
			matrix.scale(scale, scale, scale);
			matrix.translate(0, (1 / scale), 0);

			if (partialTick > 0.95f && blockEntity.getTrophyData().isOriginalTrophy()) {
				float colourMod = entity.level().random.nextFloat() * 0.7f + 0.3f;

				ParticleBuilder.forPosition(AoAParticleTypes.GENERIC_DUST.get(), blockEntity.getBlockPos().getX() + 0.5f, blockEntity.getBlockPos().getY() + 0.9 + ((entity.getBbHeight() / 2f) * scale), blockEntity.getBlockPos().getZ() + 0.5f)
						.scaleMod(0.005f)
						.lifespan(Mth.ceil(10 * (entity.level().random.nextFloat() * 0.8f + 0.2f)))
						.colourOverride(colourMod, colourMod * 200 / 255f, 0, 1f)
						.power(new Vec3(RandomUtil.randomGaussianValue() * 0.5f, RandomUtil.randomGaussianValue() * 0.5f, RandomUtil.randomGaussianValue() * 0.5f))
						.spawnParticles(entity.level());
			}

			if (AoAConfigs.CLIENT.rotatingTrophies.get())
				matrix.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, blockEntity.getPrevMobRotation(), blockEntity.getMobRotation()) * 30.0F));

			Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, matrix, buffer, combinedLight);
			matrix.popPose();
		}
	}
}