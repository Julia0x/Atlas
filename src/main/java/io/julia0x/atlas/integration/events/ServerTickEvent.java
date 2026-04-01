package io.julia0x.atlas.integration.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Fired on every server-side game tick.
 * Only on dedicated servers or when the server thread is active.
 */
public class ServerTickEvent extends Event {
    private final long tickCount;

    /**
     * Creates a new server tick event.
     *
     * @param tickCount The current tick number
     */
    public ServerTickEvent(long tickCount) {
        this.tickCount = tickCount;
    }

    /**
     * Gets the current tick count.
     *
     * @return The tick number
     */
    public long getTickCount() {
        return tickCount;
    }
}
