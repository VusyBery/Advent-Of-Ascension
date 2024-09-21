package net.tslat.aoa3.common.registration.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.entity.misc.PixonEntity;
import net.tslat.aoa3.content.world.spawner.AoACustomSpawner;
import net.tslat.aoa3.content.world.spawner.PixonSpawner;
import net.tslat.aoa3.content.world.spawner.RoamingTraderSpawner;

public final class AoACustomSpawners {
    public static final Codec<AoACustomSpawner<?>> CODEC = Codec.lazyInitialized(() -> AoARegistries.CUSTOM_SPAWNER_TYPES.registry().get().byNameCodec().dispatch("spawner", AoACustomSpawner::getType, AoACustomSpawner.Type::codec));

    public static void init() {}

    public static final DeferredHolder<AoACustomSpawner.Type<?>, AoACustomSpawner.Type<Mob>> ROAMING_TRADER = register("roaming_trader", RoamingTraderSpawner.CODEC);
    public static final DeferredHolder<AoACustomSpawner.Type<?>, AoACustomSpawner.Type<PixonEntity>> PIXONS = register("pixons", PixonSpawner.CODEC);

    private static <E extends Entity, S extends AoACustomSpawner<E>> DeferredHolder<AoACustomSpawner.Type<?>, AoACustomSpawner.Type<E>> register(String id, MapCodec<S> codec) {
        return AoARegistries.CUSTOM_SPAWNER_TYPES.register(id, () -> new AoACustomSpawner.Type<>(codec));
    }
}
