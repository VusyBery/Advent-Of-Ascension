package net.tslat.aoa3.client.render.dimension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import org.jetbrains.annotations.Nullable;

public class VoidSkyRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("void_sky");

    VoidSkyRenderingEffects() {
        super(Float.NaN, false, SkyType.NONE, false, true);

        noWeather();
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