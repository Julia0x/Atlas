package io.julia0x.atlas.core.api.handler;

/**
 * Base interface for handlers that manage specific aspects of the mod.
 * Handlers are initialized once and can receive events or perform periodic tasks.
 */
public interface IHandler {
    /**
     * Initializes the handler.
     * Called once during mod initialization.
     *
     * @return true if initialization was successful, false otherwise
     */
    boolean initialize();

    /**
     * Shuts down the handler.
     * Called when the mod is being unloaded.
     */
    void shutdown();

    /**
     * Checks if the handler is currently enabled.
     *
     * @return true if the handler is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Gets the name of this handler.
     *
     * @return The handler name (e.g., "InputHandler")
     */
    String getName();

    /**
     * Gets the priority of this handler.
     * Higher priority handlers are initialized first.
     * Default is 0.
     *
     * @return The priority level
     */
    default int getPriority() {
        return 0;
    }
}
