package net.tslat.aoa3.client.render.dimension;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import org.jetbrains.annotations.Nullable;

public class AbyssRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("abyss");

    AbyssRenderingEffects() {
        super(Float.NaN, true, SkyType.NONE, false, false);

        noWeather();
    }

    @Override
    public void adjustFogRender(ClientLevel level, FogRenderer.FogMode fogMode, FogType fogType, Camera camera, FloatConsumer farPlaneDistance, FloatConsumer nearPlaneDistance) {
        final float mod = Math.max(0, (float)camera.getPosition().y - 180f);

        farPlaneDistance.accept(20 + mod);
        nearPlaneDistance.accept(-50 + mod);
    }

    @Override
    public boolean isFoggyAt(int xCoord, int yCoord) {
        return true;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float dayProgress, float partialTick) {
        return new float[] {0, 0, 0, 0};
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColour, float brightness) {
        return fogColour.scale(brightness * (float)((Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y - 50) / (float)ClientOperations.getLevel().getMaxBuildHeight()));
    }
}
