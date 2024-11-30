package net.tslat.aoa3.event.dynamic;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Special-handler class that allows for efficient event subscription/unsubscription at the cost of loss of prioritisation and cancellation-handling policy.
 * <p>
 * This gives a performant entrypoint for event listeners that may or may not be active
 */
public final class DynamicEventSubscriptions {
    private static final ListMultimap<Class<? extends Event>, DynamicEventSubscriber<? extends Event>> LISTENERS = MultimapBuilder.hashKeys().arrayListValues().build();
    private static final List<DynamicEventSubscriber<? extends Event>> NEW_LISTENERS = new ObjectArrayList<>();

    public static void init() {
        NeoForge.EVENT_BUS.addListener(DynamicEventSubscriptions::addNewSubscribers);
    }

    /**
     * Add a group of listeners to the dynamic event handler, to be checked and organised at the end of the current tick.
     * <p>
     * This also acts as an add/remove entrypoint, since it's functionally the same thing
     */
    public static void addListeners(DynamicEventSubscriber<? extends Event>... subscribers) {
        addListeners(List.of(subscribers));
    }

    /**
     * Mark a group of subscribers as dirty, to be checked and organised at the end of the current tick.
     * <p>
     * This also acts as an add/remove entrypoint, since it's functionally the same thing
     */
    public static void addListeners(Collection<? extends DynamicEventSubscriber<? extends Event>> subscribers) {
        NEW_LISTENERS.addAll(subscribers);
    }

    private static void addNewSubscribers(final ServerTickEvent.Post ev) {
        final IEventBus eventBus = NeoForge.EVENT_BUS;

        synchronized (LISTENERS) {
            for (DynamicEventSubscriber<? extends Event> subscriber : NEW_LISTENERS) {
                if (!subscriber.isStillValid())
                    continue;

                final Class<? extends Event> clazz = subscriber.eventClass();

                if (!LISTENERS.containsKey(clazz)) {
                    eventBus.addListener(clazz, event -> {
                        for (Iterator<DynamicEventSubscriber<? extends Event>> iterator = LISTENERS.get(clazz).iterator(); iterator.hasNext();) {
                            final DynamicEventSubscriber listener = iterator.next();

                            if (!listener.isStillValid()) {
                                iterator.remove();

                                continue;
                            }

                            listener.accept(event);
                        }
                    });
                }

                final List<DynamicEventSubscriber<? extends Event>> listeners = LISTENERS.get(clazz);

                if (!listeners.contains(subscriber))
                    listeners.add(subscriber);
            }
        }

        NEW_LISTENERS.clear();
    }
}
