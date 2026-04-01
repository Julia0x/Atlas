package io.julia0x.atlas.integration.logging;

import io.julia0x.atlas.core.api.event.Event;
import io.julia0x.atlas.core.api.event.IEventListener;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.logging.Logger;

/**
 * Listener that logs event dispatches for debugging.
 * Can be enabled to trace event flow through the system.
 */
public class EventLogger implements IEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogger.class);

    private boolean enabled = false;
    private boolean logCancelled = true;

    /**
     * Enables or disables event logging.
     *
     * @param enabled true to enable logging, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            LOGGER.info("Event logging enabled");
        } else {
            LOGGER.info("Event logging disabled");
        }
    }

    /**
     * Sets whether to log cancelled events.
     *
     * @param logCancelled true to log cancelled events
     */
    public void setLogCancelled(boolean logCancelled) {
        this.logCancelled = logCancelled;
    }

    @Override
    public void onEvent(Event event) {
        if (!enabled) {
            return;
        }

        if (event.isCancelled() && !logCancelled) {
            return;
        }

        String status = event.isCancelled() ? "[CANCELLED] " : "";
        LOGGER.fine(status + "Event: " + event.getClass().getSimpleName() + " at " + event.getTimestamp());
    }

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE; // Lowest priority - log after other listeners process
    }
}
