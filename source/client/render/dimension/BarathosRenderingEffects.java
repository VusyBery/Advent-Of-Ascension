package net.tslat.aoa3.client.render.dimension;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.particletype.CustomisableParticleType;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BarathosRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("barathos");

    private float currentLight = 15;

    BarathosRenderingEffects() {
        super(192, true, DimensionSpecialEffects.SkyType.NORMAL, false, false);
    }

    @Override
    public void adjustFogRender(ClientLevel level, FogRenderer.FogMode fogMode, FogType fogType, Camera camera, FloatConsumer farPlaneDistance, FloatConsumer nearPlaneDistance) {
        final float cameraHeight = (float)camera.getPosition().y;

        if (cameraHeight < 80 || (cameraHeight > 125 && fogMode == FogRenderer.FogMode.FOG_SKY))
            return;

        float skyLight = level.getBrightness(LightLayer.SKY, camera.getBlockPosition());

        if (currentLight != skyLight) {
            final float signum = Math.signum(skyLight - currentLight);
            skyLight = (currentLight = Mth.clamp(currentLight + signum * (signum < 0 ? 0.005f : 0.01f), 0, 15));
        }

        if (skyLight == 0)
            return;

        final float delta = Math.abs(cameraHeight - 105);
        final float fogCeiling = Math.max(0, cameraHeight - 142);
        final float caveDelta = Math.max(0, 1 - (skyLight / 15f));

        farPlaneDistance.accept(Mth.clamp(150 - delta * 4, 50, 150) + fogCeiling);
        nearPlaneDistance.accept(Mth.clamp(-150 + delta * 7 + caveDelta * 170, -150, 20) + fogCeiling);
    }

    @Override
    public void spawnAmbientParticle(ClientLevel level, BlockPos pos, Biome biome) {
        if (pos.getY() >= 90 && pos.getY() <= 125 && 0.01428f < level.random.nextFloat() && level.getBrightness(LightLayer.SKY, pos) == 15) {
            float rotProgress = ((level.getGameTime() / 60f) % 360) * Mth.DEG_TO_RAD;
            Vec3 angle = new Vec3(Math.cos(rotProgress), 0, Math.sin(rotProgress)).scale(Mth.sin(((level.getGameTime() / (level.getGameTime() % 1000 > 250 ? 20f : 1f)) % 360) * Mth.DEG_TO_RAD) * 0.4f + 0.6f);

            ParticleBuilder.forPositions(new CustomisableParticleType.Data(AoAParticleTypes.SANDSTORM.get(), 0.1f, 5, 0xC4C0A1), Vec3.atLowerCornerOf(pos).add(level.random.nextDouble(), level.random.nextDouble(), level.random.nextDouble()))
                    .velocity(angle)
                    .spawnParticles(level);
        }
    }

    @Override
    public boolean isFoggyAt(int posX, int posY) {
        return posY > 85 && posY < 105;
    }

    @Override
    public @Nullable float[] getSunriseColor(float timeOfDay, float partialTick) {
        return super.getSunriseColor(timeOfDay, partialTick);
    }

    @Override
    public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {

    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColour, float brightness) {
        double delta = 1 - Mth.clamp((70 - ClientOperations.getPlayer().getY()) / 10f, 0, 1);

        return fogColour.multiply(Math.max(168 / 255f, delta), Math.max(19 / 255f, delta), Math.max(0, delta));
    }
}
