package net.tslat.aoa3.client.render.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import java.util.function.Function;

public class TexturedProjectileRenderer<T extends Entity> extends EntityRenderer<T> {
	protected final ResourceLocation texture;
	protected RenderType renderType;
	protected float scale = 0.5f;

	public TexturedProjectileRenderer(final EntityRendererProvider.Context renderManager, final ResourceLocation texture) {
		super(renderManager);

		this.texture = texture;
		this.renderType = RenderType.entityCutoutNoCull(texture);
	}

	public TexturedProjectileRenderer<T> withScale(float scale) {
		this.scale = scale;

		return this;
	}

	public TexturedProjectileRenderer<T> withRenderType(Function<ResourceLocation, RenderType> renderTypeFunction) {
		this.renderType = renderTypeFunction.apply(this.texture);

		return this;
	}

	@Override
	public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		poseStack.scale(this.scale, this.scale, this.scale);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180));

		PoseStack.Pose poseState = poseStack.last();
		Matrix4f pose = poseState.pose();
		VertexConsumer vertexConsumer = buffer.getBuffer(this.renderType);

		vertex(vertexConsumer, pose, poseState, packedLight, 0, 0, 0, 1);
		vertex(vertexConsumer, pose, poseState, packedLight, 1, 0, 1, 1);
		vertex(vertexConsumer, pose, poseState, packedLight, 1, 1, 1, 0);
		vertex(vertexConsumer, pose, poseState, packedLight, 0, 1, 0, 0);
		poseStack.popPose();
		
		super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
	}

	protected static void vertex(VertexConsumer vertexConsumer, Matrix4f pose, PoseStack.Pose poseState, int packedLight, float x, float y, float u, float v) {
		vertexConsumer.addVertex(pose, x - 0.5F, y - 0.25f, 0).setColor(0xFFFFFFFF).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseState, 0, 1, 0);
	}

	@Override
	public ResourceLocation getTextureLocation(T projectile) {
		return this.texture;
	}
}