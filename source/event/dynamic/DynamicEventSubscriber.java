package net.tslat.aoa3.event.dynamic;

import net.neoforged.bus.api.Event;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Interface for the handler/listener of an event as managed by {@link DynamicEventSubscriptions}
 */
public interface DynamicEventSubscriber<T extends Event> extends Consumer<T> {
    /**
     * The class of the event to listen to
     */
    Class<T> eventClass();

    /**
     * Whether the handler should be considered valid or not.
     * This should be considered a getter for whether this subscriber should be discarded.
     * <p>
     * Returning false will prevent this listener from being added to {@link DynamicEventSubscriptions}, and will remove it if it is already added
     */
    boolean isStillValid();

    /**
     * Create a new generic immutable DynamicEventHandler
     */
    static <T extends Event> DynamicEventSubscriber<T> of(Class<T> eventClass, Consumer<T> handler, BooleanSupplier isActive) {
        return new Impl<>(eventClass, handler, isActive);
    }

    record Impl<T extends Event>(Class<T> eventClass, Consumer<T> handler, BooleanSupplier activePredicate) implements DynamicEventSubscriber<T> {
        @Override
        public boolean isStillValid() {
            return this.activePredicate.getAsBoolean();
        }

        @Override
        public void accept(T t) {
            this.handler.accept(t);
        }
    }
}
