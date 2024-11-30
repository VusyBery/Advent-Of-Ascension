package net.tslat.aoa3.content.world.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.WorldEventSyncPacket;
import net.tslat.aoa3.common.registration.AoARegistries;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;

public class AoAWorldEventManager extends SavedData {
    private static final Factory<AoAWorldEventManager> FACTORY = new Factory<>(AoAWorldEventManager::new, AoAWorldEventManager::new, null);
    private static final String DATA_KEY = "aoa_world_events";
    private static final AoAWorldEventManager CLIENT_INSTANCE = new AoAWorldEventManager();
    private final Map<ResourceLocation, AoAWorldEvent> events = new Object2ObjectArrayMap<>(2);
    private CompoundTag initData = null;

    private AoAWorldEventManager() {}

    private AoAWorldEventManager(CompoundTag tag, HolderLookup.Provider registryLookup) {
        this.initData = tag;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        for (AoAWorldEvent event : this.events.values()) {
            CompoundTag saveData = event.save();

            if (saveData != null)
                tag.put(event.getId().toString(), saveData);
        }

        return tag;
    }

    public static AoAWorldEvent getEventById(Level level, ResourceLocation id) {
        return getForLevel(level).events.getOrDefault(id, null);
    }

    public static boolean isEventActive(Level level, ResourceLocation id) {
        AoAWorldEventManager manager = getForLevel(level);

        return manager.events.containsKey(id) && manager.events.get(id).isActive();
    }

    public static void tick(Level level) {
        getForLevel(level).events.values().forEach(ev -> ev.tick(level));
    }

    public static void syncToPlayer(ServerPlayer pl) {
        AoAWorldEventManager manager = getForLevel(pl.level());
        List<Triple<AoAWorldEvent.Type<?>, ResourceLocation, CompoundTag>> events = new ObjectArrayList<>(manager.events.size());

        for (AoAWorldEvent event : manager.events.values()) {
            events.add(Triple.of(event.getType(), event.getId(), event.save()));
        }

        AoANetworking.sendToPlayer(pl, new WorldEventSyncPacket(events));
    }

    public static void syncFromServer(List<Triple<AoAWorldEvent.Type<?>, ResourceLocation, CompoundTag>> events) {
        CLIENT_INSTANCE.events.clear();

        for (Triple<AoAWorldEvent.Type<?>, ResourceLocation, CompoundTag> event : events) {
            syncFromServer(event.getLeft(), event.getMiddle(), event.getRight());
        }
    }

    public static void syncFromServer(AoAWorldEvent.Type<?> eventType, ResourceLocation id, CompoundTag tag) {
        CLIENT_INSTANCE.events.computeIfAbsent(id, key -> eventType.clientConstructor().get()).load(tag);
    }

    public static void load(ServerLevel level) {
        List<AoAWorldEvent> events = level.getServer().registryAccess().registryOrThrow(AoARegistries.WORLD_EVENTS_REGISTRY_KEY).stream()
                .filter(event -> event.shouldAddToDimension(level))
                .map(AoAWorldEvent::copy)
                .toList();

        if (!events.isEmpty()) {
            AoAWorldEventManager manager = getForLevel(level);

            for (AoAWorldEvent event : events) {
                manager.events.put(event.getId(), event);
            }

            if (manager.initData != null) {
                for (AoAWorldEvent event : manager.events.values()) {
                    if (manager.initData.contains(event.getId().toString()))
                        event.load(manager.initData.getCompound(event.getId().toString()));
                }

                manager.initData = null;
            }
        }
    }

    public static AoAWorldEventManager getForLevel(Level level) {
        if (level.isClientSide)
            return CLIENT_INSTANCE;

        return ((ServerLevel)level).getDataStorage().computeIfAbsent(FACTORY, DATA_KEY);
    }

    public static void markDirty(ServerLevel level) {
        getForLevel(level).setDirty();
    }
}
