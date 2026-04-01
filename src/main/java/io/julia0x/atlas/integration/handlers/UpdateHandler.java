package io.julia0x.atlas.integration.handlers;

import io.julia0x.atlas.core.api.handler.IHandler;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.logging.Logger;

/**
 * Handles periodic updates and scheduled tasks.
 * Manages the mod's update loop and tick-based operations.
 */
public class UpdateHandler implements IHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHandler.class);

    private boolean enabled = false;
    private long tickCount = 0;
    private long lastUpdateTime = 0;
    private int updateFrequency = 20; // Default: every 20 ticks

    @Override
    public boolean initialize() {
        try {
            LOGGER.info("Initializing UpdateHandler");
            lastUpdateTime = System.currentTimeMillis();
            enabled = true;
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize UpdateHandler: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down UpdateHandler");
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "UpdateHandler";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    /**
     * Called on every game tick.
     * Approximately 20 times per second on the client/server.
     */
    public void onTick() {
        if (!enabled) {
            return;
        }

        tickCount++;

        // Perform updates at the specified frequency
        if (tickCount % updateFrequency == 0) {
            onUpdate();
        }
    }

    /**
     * Called periodically based on updateFrequency.
     * Override or extend this for periodic updates.
     */
    public void onUpdate() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        LOGGER.finest("Update tick: " + tickCount + ", delta: " + deltaTime + "ms");
    }

    /**
     * Gets the current tick count.
     *
     * @return The number of ticks since initialization
     */
    public long getTickCount() {
        return tickCount;
    }

    /**
     * Sets the update frequency.
     *
     * @param frequency Number of ticks between updates
     */
    public void setUpdateFrequency(int frequency) {
        if (frequency > 0) {
            this.updateFrequency = frequency;
            LOGGER.fine("Update frequency set to: " + frequency);
        }
    }

    /**
     * Gets the update frequency.
     *
     * @return The current update frequency
     */
    public int getUpdateFrequency() {
        return updateFrequency;
    }

    /**
     * Gets the time elapsed since the last update.
     *
     * @return Time in milliseconds
     */
    public long getTimeSinceLastUpdate() {
        return System.currentTimeMillis() - lastUpdateTime;
    }

    /**
     * Performs a forced update regardless of frequency.
     */
    public void forceUpdate() {
        if (enabled) {
            onUpdate();
        }
    }
}
