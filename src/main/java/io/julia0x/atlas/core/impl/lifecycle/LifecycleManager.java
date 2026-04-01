package io.julia0x.atlas.core.impl.lifecycle;

import io.julia0x.atlas.core.api.handler.IHandler;
import io.julia0x.atlas.core.impl.config.JsonConfigManager;
import io.julia0x.atlas.core.impl.event.SimpleEventBus;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages the lifecycle of the Atlas mod, including initialization and shutdown.
 * Handles loading configurations, initializing handlers, and cleaning up resources.
 */
public class LifecycleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleManager.class);

    private final SimpleEventBus eventBus;
    private final JsonConfigManager configManager;
    private final List<IHandler> handlers = new ArrayList<>();
    private boolean initialized = false;

    public LifecycleManager() {
        this.eventBus = new SimpleEventBus();
        this.configManager = new JsonConfigManager();
    }

    /**
     * Initializes the mod lifecycle.
     * Loads configuration and initializes all registered handlers.
     *
     * @return true if initialization was successful, false otherwise
     */
    public boolean initialize() {
        if (initialized) {
            LOGGER.warning("LifecycleManager already initialized");
            return false;
        }

        LOGGER.info("Starting Atlas mod initialization...");

        try {
            // Load configuration
            if (!configManager.loadConfig()) {
                LOGGER.warning("Failed to load configuration, continuing with defaults");
            }

            // Sort handlers by priority (highest first)
            handlers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

            // Initialize all handlers
            for (IHandler handler : handlers) {
                try {
                    LOGGER.info("Initializing handler: " + handler.getName());
                    if (!handler.initialize()) {
                        LOGGER.warning("Handler initialization failed: " + handler.getName());
                    }
                } catch (Exception e) {
                    LOGGER.severe("Error initializing handler " + handler.getName() + ": " + e.getMessage());
                }
            }

            initialized = true;
            LOGGER.info("Atlas mod initialization completed successfully");
            return true;
        } catch (Exception e) {
            LOGGER.severe("Unexpected error during initialization: " + e.getMessage());
            return false;
        }
    }

    /**
     * Shuts down the mod lifecycle.
     * Calls shutdown on all handlers and cleans up resources.
     */
    public void shutdown() {
        if (!initialized) {
            return;
        }

        LOGGER.info("Starting Atlas mod shutdown...");

        // Shutdown handlers in reverse order
        for (int i = handlers.size() - 1; i >= 0; i--) {
            IHandler handler = handlers.get(i);
            try {
                LOGGER.info("Shutting down handler: " + handler.getName());
                handler.shutdown();
            } catch (Exception e) {
                LOGGER.severe("Error shutting down handler " + handler.getName() + ": " + e.getMessage());
            }
        }

        // Shutdown core components
        eventBus.dispose();
        initialized = false;

        LOGGER.info("Atlas mod shutdown completed");
    }

    /**
     * Registers a handler to be initialized during mod startup.
     *
     * @param handler The handler to register
     */
    public void registerHandler(IHandler handler) {
        if (handler != null) {
            handlers.add(handler);
            LOGGER.fine("Registered handler: " + handler.getName());
        }
    }

    /**
     * Gets the event bus instance.
     *
     * @return The event bus
     */
    public SimpleEventBus getEventBus() {
        return eventBus;
    }

    /**
     * Gets the configuration manager instance.
     *
     * @return The configuration manager
     */
    public JsonConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Checks if the lifecycle manager is initialized.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Gets the number of registered handlers.
     *
     * @return Handler count
     */
    public int getHandlerCount() {
        return handlers.size();
    }
}
