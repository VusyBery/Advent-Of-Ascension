package net.tslat.aoa3.common.particleoption;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public record EntityTrackingParticleOptions(ParticleType<EntityTrackingParticleOptions> particleType, int entitySourceId) implements ParticleOptions {
	public static EntityTrackingParticleOptions ambient(Supplier<ParticleType<EntityTrackingParticleOptions>> particleType) {
		return new EntityTrackingParticleOptions(particleType.get(), -1);
	}

	public static EntityTrackingParticleOptions fromEntity(Supplier<ParticleType<EntityTrackingParticleOptions>> particleType, Entity entity) {
		return new EntityTrackingParticleOptions(particleType.get(), entity.getId());
	}

	public static MapCodec<EntityTrackingParticleOptions> codec(ParticleType<EntityTrackingParticleOptions> particleType) {
		return Codec.INT.xmap(entityId -> new EntityTrackingParticleOptions(particleType, entityId), EntityTrackingParticleOptions::entitySourceId).fieldOf("entity_id");
	}

	public static StreamCodec<? super RegistryFriendlyByteBuf, EntityTrackingParticleOptions> streamCodec(ParticleType<EntityTrackingParticleOptions> particleType) {
		return ByteBufCodecs.VAR_INT.map(entityId -> new EntityTrackingParticleOptions(particleType, entityId), EntityTrackingParticleOptions::entitySourceId);
	}

	@Override
	public ParticleType<?> getType() {
		return this.particleType;
	}
}
