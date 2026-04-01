package io.julia0x.atlas.core.api.config;

/**
 * Marker interface for objects that can be configured.
 * Implementing classes should provide configuration data through the config manager.
 */
public interface IConfigurable {
    /**
     * Gets the configuration key for this object.
     * Used to identify which config section this object uses.
     *
     * @return The configuration key (e.g., "ui", "gameplay")
     */
    String getConfigKey();

    /**
     * Called when the configuration has been reloaded.
     * Implementations should refresh their state based on the new config.
     */
    void onConfigReloaded();
}
