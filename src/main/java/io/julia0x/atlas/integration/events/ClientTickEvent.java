package io.julia0x.atlas.integration.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Fired on every client-side game tick.
 * Approximately 20 times per second.
 */
public class ClientTickEvent extends Event {
    private final long tickCount;
    private final float partialTicks;

    /**
     * Creates a new client tick event.
     *
     * @param tickCount The current tick number
     * @param partialTicks The partial tick progress (0-1)
     */
    public ClientTickEvent(long tickCount, float partialTicks) {
        this.tickCount = tickCount;
        this.partialTicks = partialTicks;
    }

    /**
     * Gets the current tick count.
     *
     * @return The tick number
     */
    public long getTickCount() {
        return tickCount;
    }

    /**
     * Gets the partial tick progress.
     *
     * @return Progress between 0 and 1
     */
    public float getPartialTicks() {
        return partialTicks;
    }
}
