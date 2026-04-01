package io.julia0x.atlas.features.ui.api;

/**
 * Represents a HUD (Heads-Up Display) element that can be rendered on screen.
 */
public interface IHudElement {
    /**
     * Gets the unique identifier for this HUD element.
     *
     * @return The element ID
     */
    String getId();

    /**
     * Gets the display name of this HUD element.
     *
     * @return The name
     */
    String getName();

    /**
     * Checks if this element is currently visible.
     *
     * @return true if visible, false if hidden
     */
    boolean isVisible();

    /**
     * Sets the visibility of this element.
     *
     * @param visible true to show, false to hide
     */
    void setVisible(boolean visible);

    /**
     * Gets the X position of this element on screen.
     *
     * @return The X coordinate
     */
    int getX();

    /**
     * Gets the Y position of this element on screen.
     *
     * @return The Y coordinate
     */
    int getY();

    /**
     * Sets the position of this element.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     */
    void setPosition(int x, int y);

    /**
     * Gets the width of this element in pixels.
     *
     * @return The width
     */
    int getWidth();

    /**
     * Gets the height of this element in pixels.
     *
     * @return The height
     */
    int getHeight();

    /**
     * Renders this HUD element.
     * Called every frame.
     *
     * @param partialTicks The partial tick time (0-1)
     */
    void render(float partialTicks);

    /**
     * Updates the element state.
     * Called before rendering.
     */
    void update();

    /**
     * Handles mouse input on this element.
     *
     * @param mouseX The mouse X position
     * @param mouseY The mouse Y position
     * @param button The mouse button (0=left, 1=right, 2=middle)
     * @return true if the event was handled, false otherwise
     */
    boolean handleMouseInput(double mouseX, double mouseY, int button);

    /**
     * Handles keyboard input on this element.
     *
     * @param key The key code
     * @param scanCode The scan code
     * @param mods The modifier keys
     * @return true if the event was handled, false otherwise
     */
    boolean handleKeyInput(int key, int scanCode, int mods);

    /**
     * Checks if this element is enabled.
     *
     * @return true if enabled, false if disabled
     */
    boolean isEnabled();

    /**
     * Sets whether this element is enabled.
     *
     * @param enabled true to enable, false to disable
     */
    void setEnabled(boolean enabled);

    /**
     * Gets the z-order (depth) of this element.
     * Higher values render on top.
     *
     * @return The z-order value
     */
    int getZOrder();
}
