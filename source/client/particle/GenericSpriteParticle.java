package net.tslat.aoa3.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.tslat.aoa3.common.particleoption.EntityTrackingParticleOptions;
import org.jetbrains.annotations.Nullable;

public class GenericSpriteParticle extends TextureSheetParticle {
    @Nullable
    private final SpriteSet sprites;

    public GenericSpriteParticle(ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, @Nullable SpriteSet sprites) {
        super(level, x, y, z, xVelocity, yVelocity, zVelocity);

        this.sprites = sprites;
        this.xd = xVelocity + (float)(this.random.nextDouble() * 2 - 1) * 0.05f;
        this.yd = yVelocity + (float)(this.random.nextDouble() * 2 - 1) * 0.05f;
        this.zd = zVelocity + (float)(this.random.nextDouble() * 2 - 1) * 0.05f;
        this.quadSize = (this.random.nextFloat() * this.random.nextFloat() * 6 + 1) * 1 / 5f;
        this.lifetime = Mth.ceil(3 * (this.random.nextFloat() * 0.8f + 0.2f));

        if (this.sprites != null)
            setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public Particle scale(float pScale) {
        return super.scale(pScale);
    }

    @Override
    public void tick() {
        if (this.sprites != null)
            setSpriteFromAge(this.sprites);

        super.tick();
    }

    @FunctionalInterface
    public interface Factory<T extends GenericSpriteParticle> {
        T create(ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, @Nullable SpriteSet sprites);
    }

    public static class Provider<T extends GenericSpriteParticle> implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final Factory<T> factory;

        public Provider(SpriteSet sprites) {
            this(sprites, (Factory)GenericSpriteParticle::new);
        }

        public Provider(SpriteSet sprites, Factory<T> factory) {
            this.sprites = sprites;
            this.factory = factory;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
            return this.factory.create(level, x, y, z, xVelocity, yVelocity, zVelocity, this.sprites);
        }
    }

    public static class SingleSpriteProvider<T extends GenericSpriteParticle> implements ParticleProvider.Sprite<EntityTrackingParticleOptions> {
        private final Factory<T> factory;

        public SingleSpriteProvider() {
            this((Factory)GenericSpriteParticle::new);
        }

        public SingleSpriteProvider(Factory<T> factory) {
            this.factory = factory;
        }

        @Override
        public TextureSheetParticle createParticle(EntityTrackingParticleOptions data, ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
            return this.factory.create(level, x, y, z, xVelocity, yVelocity, zVelocity, null);
        }
    }
}
