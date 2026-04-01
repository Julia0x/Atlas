package io.julia0x.atlas.features.gameplay.api;

/**
 * Interface for gameplay mechanics implementations.
 */
public interface IMechanics {
    /**
     * Gets the mechanic identifier.
     *
     * @return The mechanic ID
     */
    String getId();

    /**
     * Gets the mechanic display name.
     *
     * @return The mechanic name
     */
    String getName();

    /**
     * Checks if the mechanic is enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Enables or disables the mechanic.
     *
     * @param enabled true to enable, false to disable
     */
    void setEnabled(boolean enabled);

    /**
     * Gets a description of what this mechanic does.
     *
     * @return Description text
     */
    String getDescription();

    /**
     * Initializes the mechanic.
     *
     * @return true if successful, false otherwise
     */
    boolean initialize();

    /**
     * Cleans up the mechanic.
     */
    void cleanup();
}
