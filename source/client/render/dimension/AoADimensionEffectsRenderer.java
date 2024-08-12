package net.tslat.aoa3.client.render.dimension;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.library.object.ExtendedBulkSectionAccess;
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

    public void doFXTick(ClientLevel level, int playerX, int playerY, int playerZ) {
        RandomSource random = RandomSource.create();
        Block markerParticleTarget = level.getMarkerParticleTarget();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 667; i++) {
            tryFX(level, null, playerX, playerY, playerZ, 16, random, markerParticleTarget, pos);
            tryFX(level, null, playerX, playerY, playerZ, 32, random, markerParticleTarget, pos);
        }
    }

    public void tryFX(ClientLevel level, ExtendedBulkSectionAccess sectionAccess, int playerX, int playerY, int playerZ, int radius, RandomSource random, Block markerParticleTarget, BlockPos.MutableBlockPos pos) {
        int x = playerX + level.random.nextInt(radius) - level.random.nextInt(radius);
        int y = playerY + level.random.nextInt(radius) - level.random.nextInt(radius);
        int z = playerZ + level.random.nextInt(radius) - level.random.nextInt(radius);
        pos.set(x, y, z);
        BlockState block = level.getBlockState(pos);
        FluidState fluid = level.getFluidState(pos);

        block.getBlock().animateTick(block, level, pos, random);

        if (!fluid.isEmpty())
            doFluidFXTick(level, sectionAccess, fluid, block, playerX, playerY, playerZ, random, pos);

        if (markerParticleTarget == block.getBlock())
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, block), x + 0.5d, y + 0.5d, z + 0.5d, 0, 0, 0);

        if (!block.isCollisionShapeFullBlock(level, pos))
            doAmbientParticleFXTick(level, sectionAccess, playerX, playerY, playerZ, random, pos);
    }

    protected void doFluidFXTick(ClientLevel level, ExtendedBulkSectionAccess sectionAccess, FluidState fluid, BlockState block, int playerX, int playerY, int playerZ, RandomSource random, BlockPos.MutableBlockPos pos) {
        ParticleOptions dripParticle = fluid.getDripParticle();

        fluid.animateTick(level, pos, random);

        if (dripParticle != null && level.random.nextInt(10) == 0) {
            BlockPos belowPos = pos.below();

            level.trySpawnDripParticles(belowPos, level.getBlockState(belowPos), dripParticle, block.isFaceSturdy(level, pos, Direction.DOWN));
        }
    }

    protected void doAmbientParticleFXTick(ClientLevel level, ExtendedBulkSectionAccess sectionAccess, int playerX, int playerY, int playerZ, RandomSource random, BlockPos.MutableBlockPos pos) {
        Biome biome = level.getBiome(pos).value();

        if (!spawnAmbientParticle(level, sectionAccess, pos, biome)) {
            biome.getAmbientParticle()
                    .ifPresent(settings -> {
                        if (settings.canSpawn(level.random)) {
                            level.addParticle(
                                    settings.getOptions(),
                                    pos.getX() + level.random.nextDouble(),
                                    pos.getY() + level.random.nextDouble(),
                                    pos.getZ() + level.random.nextDouble(),
                                    0,
                                    0,
                                    0
                            );
                        }
                    });
        }
    }

    public boolean spawnAmbientParticle(ClientLevel level, ExtendedBulkSectionAccess sectionAccess, BlockPos pos, Biome biome) {
        return false;
    }
}
