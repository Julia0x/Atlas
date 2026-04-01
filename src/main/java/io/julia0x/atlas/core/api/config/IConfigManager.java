package io.julia0x.atlas.core.api.config;

/**
 * Manages configuration loading, storage, and access.
 * Provides a centralized way to handle all mod configuration.
 */
public interface IConfigManager {
    /**
     * Loads configuration from files.
     *
     * @return true if loading was successful, false otherwise
     */
    boolean loadConfig();

    /**
     * Saves the current configuration to files.
     *
     * @return true if saving was successful, false otherwise
     */
    boolean saveConfig();

    /**
     * Gets a configuration value by key.
     *
     * @param key The configuration key (supports nested keys like "ui.hud.enabled")
     * @param defaultValue The value to return if key is not found
     * @param <T> The expected type
     * @return The configuration value or default value
     */
    <T> T get(String key, T defaultValue);

    /**
     * Sets a configuration value by key.
     *
     * @param key The configuration key
     * @param value The value to set
     */
    void set(String key, Object value);

    /**
     * Checks if a configuration key exists.
     *
     * @param key The configuration key
     * @return true if the key exists, false otherwise
     */
    boolean has(String key);

    /**
     * Reloads all configuration from disk.
     *
     * @return true if reload was successful, false otherwise
     */
    boolean reload();

    /**
     * Registers a listener for config change events.
     *
     * @param listener The listener to notify on config changes
     */
    void registerListener(IConfigChangeListener listener);

    /**
     * Unregisters a config change listener.
     *
     * @param listener The listener to unregister
     */
    void unregisterListener(IConfigChangeListener listener);

    /**
     * Listener interface for configuration changes.
     */
    interface IConfigChangeListener {
        /**
         * Called when a configuration value changes.
         *
         * @param key The key that changed
         * @param oldValue The old value
         * @param newValue The new value
         */
        void onConfigChanged(String key, Object oldValue, Object newValue);
    }
}
