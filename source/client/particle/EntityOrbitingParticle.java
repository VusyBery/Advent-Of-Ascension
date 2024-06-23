package net.tslat.aoa3.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.common.particleoption.EntityTrackingParticleOptions;
import org.jetbrains.annotations.Nullable;

public class EntityOrbitingParticle extends TextureSheetParticle {
	private final Entity entity;
	private double startX;
	private double startY;
	private double startZ;

	public EntityOrbitingParticle(ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, SpriteSet sprites, int entitySourceId) {
		super(level, x, y, z, xVelocity, yVelocity, zVelocity);

		this.xd = xVelocity;
		this.yd = yVelocity;
		this.zd = zVelocity;
		this.quadSize = (this.random.nextFloat() * this.random.nextFloat() * 6f + 1f) * 0.25f / 5f;
		this.lifetime = Mth.ceil(5 * 5 / (this.random.nextFloat() * 0.8f + 0.2f));
		this.hasPhysics = false;
		this.entity = this.level.getEntity(entitySourceId);

		if (entity != null) {
			this.startX = this.entity.getX();
			this.startY = this.entity.getY(0.5f);
			this.startZ = this.entity.getZ();
		}

		pickSprite(sprites);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

	@Override
	public int getLightColor(float partialTick) {
		float lerpAge = Mth.clamp((this.age + partialTick) / (float)this.lifetime, 0, 1) * 240;
		int baseColour = super.getLightColor(partialTick);
		int colourMod = Math.min(240, (baseColour & 255) + (int)lerpAge);

		return colourMod | (baseColour >> 16 & 255) << 16;
	}

	@Override
	public void tick() {
		if (this.entity == null || !this.entity.isAlive()) {
			remove();

			return;
		}

		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		double slope = 20 * Mth.DEG_TO_RAD;
		double angleX = Math.cos(slope);
		double angleZ = Math.sin(slope);
		double angleY = Math.cos(angleX + angleZ);
		Vec3 rotDelta = new Vec3(
				angleX * (this.x - this.startX) - angleZ * (this.z - this.startZ),
				angleY * (this.y - this.startY) * Math.tan(angleX + angleZ),
				angleZ * (this.x - this.startX) + angleX * (this.z - this.startZ)
		).normalize().scale(2f).add(this.startX, this.startY, this.startZ);

		this.xd = rotDelta.x - this.x;
		this.yd = rotDelta.y - this.y;
		this.zd = rotDelta.z - this.z;

		move(this.xd, this.yd, this.zd);

		if (this.age++ >= this.lifetime)
			remove();
	}

	public static class Provider implements ParticleProvider<EntityTrackingParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Nullable
		@Override
		public Particle createParticle(EntityTrackingParticleOptions data, ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
			return new EntityOrbitingParticle(level, x, y, z, xVelocity, yVelocity, zVelocity, this.sprites, data.entitySourceId());
		}
	}
}