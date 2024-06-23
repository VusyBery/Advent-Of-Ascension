package net.tslat.aoa3.client.fluid.renderproperties;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.util.ColourUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class TarRenderProperties implements IClientFluidTypeExtensions {
	public static final ResourceLocation OVERLAY = AdventOfAscension.id("block/tar_overlay");
	public static final ResourceLocation UNDERWATER = AdventOfAscension.id("textures/block/tar_overlay.png");
	public static final ResourceLocation FLOWING = AdventOfAscension.id("block/tar_flow");
	public static final ResourceLocation STILL = AdventOfAscension.id("block/tar_still");

	@Override
	public int getTintColor() {
		return ColourUtil.ARGB(35, 35, 35, 255);
	}

	@Override
	public ResourceLocation getStillTexture() {
		return STILL;
	}

	@Override
	public @Nullable ResourceLocation getOverlayTexture() {
		return OVERLAY;
	}

	@Override
	public ResourceLocation getFlowingTexture() {
		return FLOWING;
	}

	@Nullable
	@Override
	public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
		return UNDERWATER;
	}

	@Override
	public void renderOverlay(Minecraft mc, PoseStack stack) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, getRenderOverlayTexture(mc));

		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		float uOffset = mc.player.getYRot() / 512f + (mc.player.tickCount / 1800f);
		float vOffset = mc.player.getXRot() / 1024f + (mc.player.tickCount / 3600f);
		Matrix4f matrixStack = stack.last().pose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 0.5f);
		buffer.addVertex(matrixStack, -1.0F, -1.0F, -0.5F).setUv(uOffset, 1 + vOffset).setNormal(0.0F, 1.0F, 0.0F);
		buffer.addVertex(matrixStack, 1.0F, -1.0F, -0.5F).setUv(1 + uOffset, 1 + vOffset).setNormal(0.0F, 1.0F, 0.0F);
		buffer.addVertex(matrixStack, 1.0F, 1.0F, -0.5F).setUv(1 + uOffset, vOffset).setNormal(0.0F, 1.0F, 0.0F);
		buffer.addVertex(matrixStack, -1.0F, 1.0F, -0.5F).setUv(uOffset, vOffset).setNormal(0.0F, 1.0F, 0.0F);
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.disableBlend();
	}
}
