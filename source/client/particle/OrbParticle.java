package net.tslat.aoa3.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class OrbParticle extends TextureSheetParticle {
	public static final ParticleRenderType GLOWING_TRANSLUCENT_TEXTURE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.depthMask(true);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);

			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		@Override
		public String toString() {
			return "GLOWING_TRANSLUCENT_TEXTURE";
		}
	};

	private float scale = 1;
	private int inGroundTime = 0;

	public OrbParticle(ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
		super(level, x, y, z, xVelocity, yVelocity, zVelocity);

		this.xd = xVelocity;
		this.yd = yVelocity;
		this.zd = zVelocity;
		this.lifetime = Mth.ceil(1 + Mth.clamp(this.random.nextGaussian(), -0.5f, 0.5f));
		this.gravity = 0.1f;
		this.friction = 0.9f;
	}

	@Override
	public void tick() {
		this.xo = x;
		this.yo = y;
		this.zo = z;

		move(this.xd, this.yd, this.zd);

		this.xd *= this.friction;
		this.yd *= this.friction;
		this.zd *= this.friction;
		this.yd -= this.gravity;

		if (onGround) {
			this.xd *= 0.699999988079071;
			this.zd *= 0.699999988079071;
			this.inGroundTime++;

			if (this.yd <= this.gravity && this.yd < -0.23f) {
				this.yd *= Math.max(-0.95f, -0.40f + this.scale / 10f);
			}
		}
		else {
			this.inGroundTime = 0;
		}

		if (this.inGroundTime > this.scale * 10)
			scale(0.7f);

		if (this.age++ >= this.lifetime || this.inGroundTime > this.scale * 100)
			remove();
	}

	@Override
	public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
		this.roll = 0.25f;

		super.render(pBuffer, pRenderInfo, pPartialTicks);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return GLOWING_TRANSLUCENT_TEXTURE;
	}

	@Override
	public Particle scale(float scale) {
		this.scale = scale;
		this.quadSize = 0.25f * scale;

		setSize(0.2f * scale, 0.2f * scale);

		return this;
	}

	@Override
	protected int getLightColor(float partialTick) {
		return LightTexture.FULL_BRIGHT - (int)(partialTick * 34);
	}

	public static class Provider implements ParticleProvider.Sprite<SimpleParticleType> {
		@Override
		public TextureSheetParticle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
			return new OrbParticle(level, x, y, z, xVelocity, yVelocity, zVelocity);
		}
	}
}