package net.tslat.aoa3.common.registration;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.particleoption.EntityTrackingParticleOptions;

import java.util.function.Function;
import java.util.function.Supplier;

public final class AoAParticleTypes {
	public static void init() {}

	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GENERIC_DUST = registerParticle("generic_dust", false);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GENERIC_SWIRL = registerParticle("generic_swirl", false);

	public static final DeferredHolder<ParticleType<?>, ParticleType<ItemParticleOption>> FLOATING_ITEM_FRAGMENT = registerParticle("floating_item_fragment", ItemParticleOption::codec, ItemParticleOption::streamCodec, false);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORB = registerParticle("orb", false);
	public static final DeferredHolder<ParticleType<?>, ParticleType<EntityTrackingParticleOptions>> FIRE_AURA = registerParticle("fire_aura", EntityTrackingParticleOptions::codec, EntityTrackingParticleOptions::streamCodec, true);

	public static final DeferredHolder<ParticleType<?>, ParticleType<EntityTrackingParticleOptions>> FREEZING_SNOWFLAKE = registerParticle("freezing_snowflake", EntityTrackingParticleOptions::codec, EntityTrackingParticleOptions::streamCodec, true);
	public static final DeferredHolder<ParticleType<?>, ParticleType<EntityTrackingParticleOptions>> BURNING_FLAME = registerParticle("burning_flame", EntityTrackingParticleOptions::codec, EntityTrackingParticleOptions::streamCodec, true);
	public static final DeferredHolder<ParticleType<?>, ParticleType<EntityTrackingParticleOptions>> SANDSTORM = registerParticle("sandstorm", EntityTrackingParticleOptions::codec, EntityTrackingParticleOptions::streamCodec, true);

	private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> registerParticle(String id, Function<ParticleType<T>, MapCodec<T>> codec, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec, boolean alwaysVisible) {
		return registerParticle(id, () -> new ParticleType<T>(alwaysVisible) {
			@Override
			public MapCodec<T> codec() {
				return codec.apply(this);
			}

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return streamCodec.apply(this);
			}
		});
	}

	private static DeferredHolder<ParticleType<?>, SimpleParticleType> registerParticle(String id, boolean bypassDistanceLimit) {
		return AoARegistries.PARTICLES.register(id, () -> new SimpleParticleType(bypassDistanceLimit));
	}

	private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> registerParticle(String id, Supplier<? extends ParticleType<T>> particle) {
		return AoARegistries.PARTICLES.register(id, particle);
	}
}
