package io.julia0x.atlas.core.api.event;

/**
 * Base class for all events in the Atlas mod.
 * Events are immutable once created and can be posted to the event bus.
 */
public abstract class Event {
    private final long timestamp;
    private boolean cancelled = false;

    /**
     * Creates a new event with the current timestamp.
     */
    public Event() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets the timestamp when this event was created.
     *
     * @return The event creation timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if this event has been cancelled.
     *
     * @return true if the event is cancelled, false otherwise
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Cancels this event. Some listeners may respect this flag to prevent further processing.
     *
     * @param cancelled whether to cancel the event
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{timestamp=" + timestamp + ", cancelled=" + cancelled + "}";
    }
}
