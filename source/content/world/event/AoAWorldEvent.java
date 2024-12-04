package net.tslat.aoa3.content.world.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.WorldEventUpdatePacket;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface AoAWorldEvent {
    void setActive(boolean active);
    boolean isActive();
    boolean shouldAddToDimension(ServerLevel level);
    void tick(Level level);
    AoAWorldEvent copy();
    ResourceLocation getId();
    Type<? extends AoAWorldEvent> getType();
    CompoundTag save();
    void load(CompoundTag tag);
    void start(ServerLevel level);
    void stop(ServerLevel level);

    default void markDirty(ServerLevel level) {
        AoAWorldEventManager.markDirty(level);
        AoANetworking.sendToAllInLevel(level, new WorldEventUpdatePacket(getType(), getId(), save()));
    }

    record Type<E extends AoAWorldEvent>(MapCodec<E> codec, Supplier<E> clientConstructor) {}
    record GenericSettings(ResourceLocation id, Set<ResourceKey<Level>> dimensions) {
        public static final Codec<GenericSettings> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(GenericSettings::id),
                ResourceKey.codec(Registries.DIMENSION).listOf().xmap(Set::copyOf, List::copyOf).fieldOf("dimensions").forGetter(GenericSettings::dimensions)
        ).apply(builder, GenericSettings::new));
    }
}
