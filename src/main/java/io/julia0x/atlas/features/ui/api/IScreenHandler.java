package io.julia0x.atlas.features.ui.api;

/**
 * Handles screen-related operations and events.
 */
public interface IScreenHandler {
    /**
     * Called when a screen is opened.
     *
     * @param screenName The name/class of the screen
     */
    void onScreenOpen(String screenName);

    /**
     * Called when a screen is closed.
     *
     * @param screenName The name/class of the screen
     */
    void onScreenClose(String screenName);

    /**
     * Checks if a specific screen type is currently open.
     *
     * @param screenName The screen name to check
     * @return true if open, false otherwise
     */
    boolean isScreenOpen(String screenName);

    /**
     * Gets the current open screen name.
     *
     * @return The screen name, or null if no screen is open
     */
    String getCurrentScreen();

    /**
     * Closes the currently open screen.
     */
    void closeCurrentScreen();
}
