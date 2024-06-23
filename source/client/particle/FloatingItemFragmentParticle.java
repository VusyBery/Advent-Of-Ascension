package net.tslat.aoa3.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FloatingItemFragmentParticle extends BreakingItemParticle {
	public FloatingItemFragmentParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ItemStack stack) {
		super(level, x, y, z, stack);

		this.xd = this.xd * 0.1f + velocityX;
		this.yd = this.yd * 0.1f + velocityY;
		this.zd = this.zd * 0.1f + velocityZ;
		this.lifetime *= 2;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level.isWaterAt(BlockPos.containing(this.x, this.y, this.z))) {
			this.xd *= 0.1f;
			this.yd *= 0.01f;
			this.zd *= 0.1f;
		}
	}

	public static class Factory implements ParticleProvider<ItemParticleOption> {
		@Nullable
		@Override
		public Particle createParticle(ItemParticleOption data, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new FloatingItemFragmentParticle(level, x, y, z, velocityX, velocityY, velocityZ, data.getItem());
		}
	}
}