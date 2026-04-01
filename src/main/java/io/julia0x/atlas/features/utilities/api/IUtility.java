package io.julia0x.atlas.features.utilities.api;

/**
 * Interface for utility tools and helpers.
 */
public interface IUtility {
    /**
     * Gets the utility identifier.
     *
     * @return The utility ID
     */
    String getId();

    /**
     * Gets the utility display name.
     *
     * @return The utility name
     */
    String getName();

    /**
     * Gets a description of what this utility does.
     *
     * @return Description text
     */
    String getDescription();

    /**
     * Checks if the utility is available.
     *
     * @return true if available, false otherwise
     */
    boolean isAvailable();

    /**
     * Checks if the utility is enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Enables or disables the utility.
     *
     * @param enabled true to enable, false to disable
     */
    void setEnabled(boolean enabled);

    /**
     * Initializes the utility.
     *
     * @return true if successful, false otherwise
     */
    boolean initialize();

    /**
     * Cleans up the utility.
     */
    void cleanup();

    /**
     * Gets the configuration key for this utility.
     *
     * @return The config key
     */
    String getConfigKey();
}
