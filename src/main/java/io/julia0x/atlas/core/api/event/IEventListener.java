package io.julia0x.atlas.core.api.event;

/**
 * Represents a listener that can handle events.
 * Implementations should process the given event.
 */
public interface IEventListener {
    /**
     * Called when an event is dispatched to this listener.
     *
     * @param event The event being dispatched
     */
    void onEvent(Event event);

    /**
     * Gets the priority of this listener.
     * Higher priority listeners are called first.
     * Default is 0.
     *
     * @return The priority level
     */
    default int getPriority() {
        return 0;
    }
}
