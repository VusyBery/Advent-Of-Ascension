package net.tslat.aoa3.client.render.dimension;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class AoADimensionEffectsRenderer extends DimensionSpecialEffects {
    protected static final ResourceLocation MOON_TEXTURE = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");
    protected static final ResourceLocation SUN_TEXTURE = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");

    protected final boolean noClouds;
    protected final boolean noSky;
    protected boolean noWeather = false;

    protected AoADimensionEffectsRenderer(float cloudHeight, boolean hasGround, SkyType skyType, boolean forceBrightLightmap, boolean constantAmbientLight) {
        super(cloudHeight, hasGround, skyType, forceBrightLightmap, constantAmbientLight);

        this.noClouds = Float.isNaN(cloudHeight);
        this.noSky = skyType == SkyType.NONE;
    }

    protected void noWeather() {
        this.noWeather = true;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return super.getSunriseColor(timeOfDay, partialTick);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColour, float brightness) {
        return fogColour.multiply(brightness * 0.94f + 0.06F, brightness * 0.94f + 0.06f, brightness * 0.91f + 0.09f);
    }

    @Override
    public boolean isFoggyAt(int posX, int posY) {
        return false;
    }

    @Override
    public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        return this.noClouds;
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        return this.noSky;
    }

    @Override
    public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
        return this.noWeather;
    }

    @Override
    public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
        return this.noWeather;
    }

    @Override
    public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {}

    public void adjustFogRender(ClientLevel level, FogRenderer.FogMode fogMode, FogType fogType, Camera camera, FloatConsumer farPlaneDistance, FloatConsumer nearPlaneDistance) {}

    public void spawnAmbientParticle(ClientLevel level, BlockPos pos, Biome biome) {}
}
