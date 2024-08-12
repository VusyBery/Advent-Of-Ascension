package net.tslat.aoa3.client.render.dimension;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.particleoption.EntityTrackingParticleOptions;
import net.tslat.aoa3.common.registration.AoAParticleTypes;
import net.tslat.aoa3.common.registration.custom.AoAWorldEvents;
import net.tslat.aoa3.content.world.event.AoAWorldEventManager;
import net.tslat.aoa3.content.world.event.BarathosSandstormEvent;
import net.tslat.aoa3.library.object.ExtendedBulkSectionAccess;
import net.tslat.effectslib.api.particle.ParticleBuilder;
import org.jetbrains.annotations.Nullable;

public class BarathosRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("barathos");

    private float currentLight = 15;
    private BarathosSandstormEvent sandstorm = null;

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
    public void doFXTick(ClientLevel level, int playerX, int playerY, int playerZ) {
        this.sandstorm = AoAWorldEventManager.getEventById(level, AoAWorldEvents.BARATHOS_SANDSTORM.getId()) instanceof BarathosSandstormEvent event && event.isActive() ? event : null;

        super.doFXTick(level, playerX, playerY, playerZ);
    }

    @Override
    public boolean spawnAmbientParticle(ClientLevel level, ExtendedBulkSectionAccess sectionAccess, BlockPos pos, Biome biome) {
        if (pos.getY() <= 125 && 0.98572f >= level.random.nextFloat()) {
            if (pos.getY() >= 90) {
                if (this.sandstorm != null) {
                    if (level.getBrightness(LightLayer.SKY, pos) == 15) {
                        float intensity = this.sandstorm.getIntensity(level.getGameTime());
                        float rotProgress = ((level.getGameTime() / 60f) % 360) * Mth.DEG_TO_RAD;
                        Vec3 angle = new Vec3(Math.cos(rotProgress), 0, Math.sin(rotProgress)).scale(Mth.sin(((level.getGameTime() / (level.getGameTime() % 1000 > 250 ? 20f : 1f)) % 360) * Mth.DEG_TO_RAD) * 0.3f * intensity + 0.9f);

                        ParticleBuilder.forPositions(EntityTrackingParticleOptions.ambient(AoAParticleTypes.SANDSTORM), Vec3.atLowerCornerOf(pos).add(level.random.nextDouble(), level.random.nextDouble(), level.random.nextDouble()))
                                .scaleMod(0.3f * intensity)
                                .lifespan(Mth.ceil(5 / (level.random.nextFloat() * 0.8f + 0.2f)))
                                .colourOverride(0xC4C0A1)
                                .velocity(angle)
                                .spawnParticles(level);
                    }
                }
            }
            else if (0.1f >= level.random.nextFloat()) {
                ParticleBuilder.forPositions(ParticleTypes.SMOKE, Vec3.atLowerCornerOf(pos).add(level.random.nextDouble(), level.random.nextDouble(), level.random.nextDouble()))
                        .lifespan(Mth.ceil(5 / (level.random.nextFloat() * 0.8f + 0.2f)))
                        //.scaleMod(0.5f)
                        .velocity(0, 0.05f, 0)
                        .spawnParticles(level);
            }
        }

        return true;
    }

    @Override
    public boolean isFoggyAt(int posX, int posY) {
        return posY > 85 && posY < 105;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return super.getSunriseColor(timeOfDay, partialTick);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColour, float brightness) {
        double delta = 1 - Mth.clamp((70 - ClientOperations.getPlayer().getY()) / 10f, 0, 1);

        return fogColour.multiply(Math.max(168 / 255f, delta), Math.max(19 / 255f, delta), Math.max(0, delta));
    }
}
