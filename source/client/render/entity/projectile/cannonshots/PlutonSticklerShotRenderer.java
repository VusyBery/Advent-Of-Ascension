package net.tslat.aoa3.client.render.entity.projectile.cannonshots;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.tslat.aoa3.common.particletype.CustomisableParticleType;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.object.entity.projectile.cannon.PlutonSticklerShotEntity;
import net.tslat.aoa3.util.ColourUtil;

import javax.annotation.Nullable;

public class PlutonSticklerShotRenderer extends EntityRenderer<PlutonSticklerShotEntity> {
	private final ResourceLocation texture;

	public PlutonSticklerShotRenderer(final EntityRendererManager manager, final ResourceLocation textureResource) {
		super(manager);
		texture = textureResource;
	}

	@Override
	public void render(PlutonSticklerShotEntity entity, float entityYaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int packedLight) {
		for (int i = 0; i < 14; i++) {
			entity.level.addParticle(new CustomisableParticleType.Data(AoAParticleTypes.SPARKLER.get(), 1, 3, ColourUtil.YELLOW), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
		}
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(PlutonSticklerShotEntity entity) {
		return texture;
	}
}
