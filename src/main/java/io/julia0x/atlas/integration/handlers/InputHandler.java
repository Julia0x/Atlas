package io.julia0x.atlas.integration.handlers;

import io.julia0x.atlas.core.api.handler.IHandler;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.logging.Logger;

/**
 * Handles keyboard and mouse input events.
 * Manages input-related integrations and event dispatching.
 */
public class InputHandler implements IHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputHandler.class);

    private boolean enabled = false;

    @Override
    public boolean initialize() {
        try {
            LOGGER.info("Initializing InputHandler");
            // Register input listeners and callbacks
            enabled = true;
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize InputHandler: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down InputHandler");
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "InputHandler";
    }

    @Override
    public int getPriority() {
        return 50; // High priority
    }

    /**
     * Handles a keyboard key press event.
     *
     * @param key The key code
     * @param mods The modifier keys pressed
     */
    public void onKeyPress(int key, int mods) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Key pressed: " + key + " with mods: " + mods);
    }

    /**
     * Handles a keyboard key release event.
     *
     * @param key The key code
     * @param mods The modifier keys pressed
     */
    public void onKeyRelease(int key, int mods) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Key released: " + key + " with mods: " + mods);
    }

    /**
     * Handles a mouse button press event.
     *
     * @param button The button (0=left, 1=right, 2=middle)
     * @param x The mouse x position
     * @param y The mouse y position
     */
    public void onMousePress(int button, double x, double y) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Mouse press: button=" + button + " pos=(" + x + "," + y + ")");
    }

    /**
     * Handles a mouse button release event.
     *
     * @param button The button
     * @param x The mouse x position
     * @param y The mouse y position
     */
    public void onMouseRelease(int button, double x, double y) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Mouse release: button=" + button + " pos=(" + x + "," + y + ")");
    }

    /**
     * Handles mouse movement.
     *
     * @param x The mouse x position
     * @param y The mouse y position
     */
    public void onMouseMove(double x, double y) {
        if (!enabled) {
            return;
        }
        // Don't log mouse movement to avoid spam
    }

    /**
     * Handles mouse scroll events.
     *
     * @param x The mouse x position
     * @param y The mouse y position
     * @param deltaY The scroll amount (positive for up, negative for down)
     */
    public void onMouseScroll(double x, double y, double deltaY) {
        if (!enabled) {
            return;
        }
        LOGGER.fine("Mouse scroll: delta=" + deltaY);
    }
}
