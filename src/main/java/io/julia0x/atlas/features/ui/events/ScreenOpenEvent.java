package io.julia0x.atlas.features.ui.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Fired when a screen is opened.
 */
public class ScreenOpenEvent extends Event {
    private final String screenName;
    private final int screenWidth;
    private final int screenHeight;

    /**
     * Creates a new screen open event.
     *
     * @param screenName The name of the screen being opened
     * @param screenWidth The screen width
     * @param screenHeight The screen height
     */
    public ScreenOpenEvent(String screenName, int screenWidth, int screenHeight) {
        this.screenName = screenName;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Gets the screen name.
     *
     * @return The screen name
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Gets the screen width.
     *
     * @return Width in pixels
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Gets the screen height.
     *
     * @return Height in pixels
     */
    public int getScreenHeight() {
        return screenHeight;
    }
}
