package net.tslat.aoa3.client.render.dimension;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.util.RenderUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class LunalusRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("lunalus");
    private static final ResourceLocation TEXTURE = AdventOfAscension.id("textures/gui/realmstonegui/background.png");

    LunalusRenderingEffects() {
        super(Float.NaN, true, SkyType.NONE, false, true);
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        PoseStack poseStack = new PoseStack();
        Tesselator tesellator = Tesselator.getInstance();

        poseStack.mulPose(frustumMatrix);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderUtil.prepRenderTexture(TEXTURE);

        for (int i = 0; i < 6; ++i) {
            poseStack.pushPose();

            switch (i) {
                case 1 -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                case 2 -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                case 3 -> poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                case 4 -> poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                case 5 -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                default -> {}
            }

            Matrix4f pose = poseStack.last().pose();
            BufferBuilder buffer = tesellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            buffer.addVertex(pose, -100, -100, -100).setUv(0.0F, 0.0F).setColor(150, 150, 150, 255);
            buffer.addVertex(pose, -100, -100, 100).setUv(0.0F, 1).setColor(150, 150, 150, 255);
            buffer.addVertex(pose, 100, -100, 100).setUv(1, 1).setColor(150, 150, 150, 255);
            buffer.addVertex(pose, 100, -100, -100).setUv(1, 0.0F).setColor(150, 150, 150, 255);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
            poseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColour, float celestialAngle) {
        return biomeFogColour;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float celestialAngle, float partialTicks) {
        return null;
    }
}
