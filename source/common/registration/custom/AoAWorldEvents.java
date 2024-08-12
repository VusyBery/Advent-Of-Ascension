package net.tslat.aoa3.common.registration.custom;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.world.event.AoAWorldEvent;
import net.tslat.aoa3.content.world.event.BarathosSandstormEvent;

import java.util.function.Supplier;

public final class AoAWorldEvents {
	public static final Codec<AoAWorldEvent> CODEC = Codec.lazyInitialized(() -> AoARegistries.WORLD_EVENT_TYPES.registry().get().byNameCodec().dispatch("world_event", AoAWorldEvent::getType, AoAWorldEvent.Type::codec));

	public static void init() {}

	public static DeferredHolder<AoAWorldEvent.Type<?>, AoAWorldEvent.Type<BarathosSandstormEvent>> BARATHOS_SANDSTORM = register("barathos_sandstorm", () -> new AoAWorldEvent.Type<>(BarathosSandstormEvent.CODEC, BarathosSandstormEvent::new));

	private static <E extends AoAWorldEvent> DeferredHolder<AoAWorldEvent.Type<?>, AoAWorldEvent.Type<E>> register(String id, Supplier<AoAWorldEvent.Type<E>> type) {
		return AoARegistries.WORLD_EVENT_TYPES.register(id, type);
	}
}
