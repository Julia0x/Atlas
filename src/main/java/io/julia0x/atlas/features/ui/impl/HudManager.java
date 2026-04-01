package io.julia0x.atlas.features.ui.impl;

import io.julia0x.atlas.core.impl.registry.SimpleRegistry;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;
import io.julia0x.atlas.features.ui.api.IHudElement;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages all HUD elements in the mod.
 * Handles registration, rendering, and input processing.
 */
public class HudManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HudManager.class);

    private final SimpleRegistry<IHudElement> elements = new SimpleRegistry<>("HUD Elements");
    private boolean enabled = false;

    /**
     * Initializes the HUD manager.
     *
     * @return true if successful, false otherwise
     */
    public boolean initialize() {
        LOGGER.info("Initializing HUD Manager");
        enabled = true;
        return true;
    }

    /**
     * Registers a HUD element.
     *
     * @param element The element to register
     * @return true if registered successfully, false if ID already exists
     */
    public boolean registerElement(IHudElement element) {
        if (element == null) {
            LOGGER.warning("Cannot register null HUD element");
            return false;
        }

        return elements.register(element.getId(), element);
    }

    /**
     * Unregisters a HUD element.
     *
     * @param elementId The element ID
     * @return true if unregistered successfully, false if not found
     */
    public boolean unregisterElement(String elementId) {
        return elements.unregister(elementId);
    }

    /**
     * Gets a registered HUD element.
     *
     * @param elementId The element ID
     * @return The element, or null if not found
     */
    public IHudElement getElement(String elementId) {
        return elements.get(elementId).orElse(null);
    }

    /**
     * Gets all registered elements sorted by z-order.
     *
     * @return List of elements sorted by z-order (lowest first)
     */
    public List<IHudElement> getElementsSorted() {
        List<IHudElement> sorted = new ArrayList<>(elements.getAll());
        sorted.sort(Comparator.comparingInt(IHudElement::getZOrder));
        return sorted;
    }

    /**
     * Renders all visible and enabled HUD elements.
     *
     * @param partialTicks The partial tick time
     */
    public void renderAll(float partialTicks) {
        if (!enabled) {
            return;
        }

        for (IHudElement element : getElementsSorted()) {
            if (element.isVisible() && element.isEnabled()) {
                try {
                    element.render(partialTicks);
                } catch (Exception e) {
                    LOGGER.severe("Error rendering HUD element '" + element.getId() + "': " + e.getMessage());
                }
            }
        }
    }

    /**
     * Updates all HUD elements.
     */
    public void updateAll() {
        if (!enabled) {
            return;
        }

        for (IHudElement element : elements.getAll()) {
            if (element.isEnabled()) {
                try {
                    element.update();
                } catch (Exception e) {
                    LOGGER.severe("Error updating HUD element '" + element.getId() + "': " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles mouse input for HUD elements.
     *
     * @param mouseX The mouse X position
     * @param mouseY The mouse Y position
     * @param button The button that was pressed
     * @return true if any element handled the input, false otherwise
     */
    public boolean handleMouseInput(double mouseX, double mouseY, int button) {
        if (!enabled) {
            return false;
        }

        // Check elements in reverse order (top elements first)
        List<IHudElement> sorted = getElementsSorted();
        for (int i = sorted.size() - 1; i >= 0; i--) {
            IHudElement element = sorted.get(i);
            if (element.isVisible() && element.isEnabled()) {
                if (element.handleMouseInput(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handles keyboard input for HUD elements.
     *
     * @param key The key code
     * @param scanCode The scan code
     * @param mods The modifier keys
     * @return true if any element handled the input, false otherwise
     */
    public boolean handleKeyInput(int key, int scanCode, int mods) {
        if (!enabled) {
            return false;
        }

        for (IHudElement element : elements.getAll()) {
            if (element.isVisible() && element.isEnabled()) {
                if (element.handleKeyInput(key, scanCode, mods)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Enables or disables the HUD manager.
     *
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks if the HUD manager is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the number of registered HUD elements.
     *
     * @return Element count
     */
    public int getElementCount() {
        return elements.size();
    }

    /**
     * Clears all HUD elements.
     */
    public void clear() {
        elements.clear();
        LOGGER.info("Cleared all HUD elements");
    }
}
