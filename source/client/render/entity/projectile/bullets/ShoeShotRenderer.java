package net.tslat.aoa3.client.render.entity.projectile.bullets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.tslat.aoa3.client.render.entity.projectile.TexturedProjectileRenderer;
import net.tslat.aoa3.content.entity.projectile.gun.ShoeShotEntity;
import org.joml.Matrix4f;


public class ShoeShotRenderer extends TexturedProjectileRenderer<ShoeShotEntity> {
	public ShoeShotRenderer(final EntityRendererProvider.Context manager, final ResourceLocation texture) {
		super(manager, texture);
	}

	@Override
	public void render(ShoeShotEntity entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		poseStack.scale(this.scale, this.scale, this.scale);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		poseStack.mulPose(Axis.XP.rotationDegrees(((entity.tickCount + partialTick) / 0.3f) * (180f / (float)Math.PI)));

		PoseStack.Pose poseState = poseStack.last();
		Matrix4f pose = poseState.pose();
		VertexConsumer vertexConsumer = buffer.getBuffer(this.renderType);

		vertex(vertexConsumer, pose, poseState, packedLight, 0, 0, 0, 1);
		vertex(vertexConsumer, pose, poseState, packedLight, 1, 0, 1, 1);
		vertex(vertexConsumer, pose, poseState, packedLight, 1, 1, 1, 0);
		vertex(vertexConsumer, pose, poseState, packedLight, 0, 1, 0, 0);
		poseStack.popPose();

		RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity, entity.getDisplayName(), this, poseStack, buffer, packedLight, partialTick);
		NeoForge.EVENT_BUS.post(renderNameTagEvent);

		if (renderNameTagEvent.canRender() != TriState.FALSE && (renderNameTagEvent.canRender() == TriState.TRUE || shouldShowName(entity)))
			renderNameTag(entity, renderNameTagEvent.getContent(), poseStack, buffer, packedLight, partialTick);
	}
}