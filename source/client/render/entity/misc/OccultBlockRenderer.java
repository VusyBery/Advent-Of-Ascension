package net.tslat.aoa3.client.render.entity.misc;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.tslat.aoa3.content.item.tool.pickaxe.OccultPickaxe;
import net.tslat.aoa3.event.GlobalEvents;
import net.tslat.aoa3.util.ColourUtil;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CopyOnWriteArrayList;

public final class OccultBlockRenderer {
	private static final RenderType CUSTOM_LINES_RENDER_TYPE = RenderType.create(
			"occult_block_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, true, false, RenderType.CompositeState.builder()
					.setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
					.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(5)))
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setOutputState(RenderStateShard.OUTLINE_TARGET)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.setCullState(RenderStateShard.NO_CULL)
					.createCompositeState(false));
	private static final CopyOnWriteArrayList<IntObjectPair<List<OccultPickaxe.LocatedBlock>>> OCCULT_BLOCKS = new CopyOnWriteArrayList<>();

	public static void init() {
		NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderLevelStageEvent.class, OccultBlockRenderer::onWorldRender);
	}

	public static void addOccultBlocks(int renderUntil, List<OccultPickaxe.LocatedBlock> blocks) {
		OCCULT_BLOCKS.add(IntObjectPair.of(renderUntil, blocks));
	}

	private static void onWorldRender(final RenderLevelStageEvent ev) {
		if (ev.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || OCCULT_BLOCKS.isEmpty())
			return;

		Minecraft mc = Minecraft.getInstance();
		boolean rendered = false;
		PoseStack poseStack = ev.getPoseStack();
		Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
		VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(CUSTOM_LINES_RENDER_TYPE);

		poseStack.pushPose();
		poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		PoseStack.Pose pose = poseStack.last();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.enableBlend();

		OCCULT_BLOCKS.removeIf(entry -> GlobalEvents.tick >= entry.leftInt());

		for (IntObjectPair<List<OccultPickaxe.LocatedBlock>> entry : OCCULT_BLOCKS) {
			for (Iterator<OccultPickaxe.LocatedBlock> iterator = entry.right().iterator(); iterator.hasNext();) {
				OccultPickaxe.LocatedBlock block = iterator.next();

				if (GlobalEvents.tick % 2 == 1 && mc.player.level().getBlockState(block.pos()) != block.state()) {
					iterator.remove();

					continue;
				}

				BlockPos pos = block.pos();
				VoxelShape shape = block.state().getShape(mc.level, pos, CollisionContext.of(mc.player));
				ColourUtil.Colour colour = block.colour();
				rendered = true;

				shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) -> {
					float lengthX = (float)(maxX - minX);
					float lengthY = (float)(maxY - minY);
					float lengthZ = (float)(maxZ - minZ);
					float length = Mth.sqrt(lengthX * lengthX + lengthY * lengthY + lengthZ * lengthZ);
					lengthX /= length;
					lengthY /= length;
					lengthZ /= length;

					buffer.addVertex(pose.pose(), (float)minX + pos.getX(), (float)minY + pos.getY(), (float)minZ + pos.getZ()).setColor(colour.red(), colour.green(), colour.blue(), colour.alpha()).setNormal(pose, lengthX, lengthY, lengthZ);
					buffer.addVertex(pose.pose(), (float)maxX + pos.getX(), (float)maxY + pos.getY(), (float)maxZ + pos.getZ()).setColor(colour.red(), colour.green(), colour.blue(), colour.alpha()).setNormal(pose, lengthX, lengthY, lengthZ);
				});
			}
		}

		poseStack.popPose();
		RenderSystem.polygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);

		if (rendered)
			mc.renderBuffers().bufferSource().endBatch();
	}
}
