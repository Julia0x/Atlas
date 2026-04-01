package io.julia0x.atlas.core.api.event;

/**
 * Central event bus for dispatching and listening to events.
 * Manages event registration, dispatch, and listener management.
 */
public interface IEventBus {
    /**
     * Registers a listener for a specific event type.
     *
     * @param eventType The event class to listen for
     * @param listener  The listener to register
     * @param <T>       The event type
     */
    <T extends Event> void subscribe(Class<T> eventType, IEventListener listener);

    /**
     * Unregisters a listener from a specific event type.
     *
     * @param eventType The event class
     * @param listener  The listener to unregister
     * @param <T>       The event type
     */
    <T extends Event> void unsubscribe(Class<T> eventType, IEventListener listener);

    /**
     * Posts an event to all registered listeners.
     * Listeners are called in priority order (highest first).
     *
     * @param event The event to post
     */
    void post(Event event);

    /**
     * Clears all registered listeners.
     */
    void clear();

    /**
     * Gets the number of listeners for a specific event type.
     *
     * @param eventType The event class
     * @return Number of registered listeners
     */
    int getListenerCount(Class<? extends Event> eventType);
}
