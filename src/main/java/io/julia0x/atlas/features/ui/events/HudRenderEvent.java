package io.julia0x.atlas.features.ui.events;

import io.julia0x.atlas.core.api.event.Event;

/**
 * Fired before HUD elements are rendered.
 */
public class HudRenderEvent extends Event {
    private final float partialTicks;
    private final int screenWidth;
    private final int screenHeight;

    /**
     * Creates a new HUD render event.
     *
     * @param partialTicks The partial tick time (0-1)
     * @param screenWidth The screen width in pixels
     * @param screenHeight The screen height in pixels
     */
    public HudRenderEvent(float partialTicks, int screenWidth, int screenHeight) {
        this.partialTicks = partialTicks;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Gets the partial tick progress.
     *
     * @return Progress between 0 and 1
     */
    public float getPartialTicks() {
        return partialTicks;
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
