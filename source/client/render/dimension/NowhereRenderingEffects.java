package net.tslat.aoa3.client.render.dimension;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.event.dimension.NowhereEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class NowhereRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("nowhere");

    private final DimensionSpecialEffects lunalusRenderer;

    NowhereRenderingEffects(DimensionSpecialEffects lunalusRenderer) {
        super(Float.NaN, false, SkyType.NONE, false, true);

        this.lunalusRenderer = lunalusRenderer;

        noWeather();
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        if (NowhereEvents.isInParkourRegion(camera.getBlockPosition()))
            lunalusRenderer.renderSky(level, ticks, partialTick, frustumMatrix, camera, projectionMatrix, isFoggy, setupFog);

        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColour, float brightness) {
        return fogColour;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return null;
    }
}