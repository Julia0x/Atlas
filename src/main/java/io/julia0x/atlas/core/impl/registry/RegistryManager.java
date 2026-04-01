package io.julia0x.atlas.core.impl.registry;

import io.julia0x.atlas.core.utils.common.ValidationUtil;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages multiple registries for different types of objects.
 * Provides a centralized way to access and manage all registries.
 */
public class RegistryManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryManager.class);

    private final Map<String, SimpleRegistry<?>> registries = new ConcurrentHashMap<>();

    /**
     * Creates or gets a registry for the given key and type.
     *
     * @param key The registry key
     * @param <T> The type of objects in the registry
     * @return The registry
     */
    public <T> SimpleRegistry<T> getOrCreate(String key) {
        ValidationUtil.requireNonEmpty(key, "Registry key cannot be empty");

        return (SimpleRegistry<T>) registries.computeIfAbsent(key, k -> {
            SimpleRegistry<T> registry = new SimpleRegistry<>(k);
            LOGGER.info("Created registry: " + k);
            return registry;
        });
    }

    /**
     * Gets an existing registry by key.
     *
     * @param key The registry key
     * @param <T> The type of objects in the registry
     * @return An Optional containing the registry, or empty if not found
     */
    public <T> Optional<SimpleRegistry<T>> get(String key) {
        return Optional.ofNullable((SimpleRegistry<T>) registries.get(key));
    }

    /**
     * Checks if a registry exists.
     *
     * @param key The registry key
     * @return true if the registry exists, false otherwise
     */
    public boolean contains(String key) {
        return registries.containsKey(key);
    }

    /**
     * Removes a registry and clears its contents.
     *
     * @param key The registry key
     * @return true if the registry was removed, false if not found
     */
    public boolean remove(String key) {
        SimpleRegistry<?> removed = registries.remove(key);
        if (removed != null) {
            removed.clear();
            LOGGER.info("Removed registry: " + key);
            return true;
        }
        return false;
    }

    /**
     * Clears all registries.
     */
    public void clear() {
        for (SimpleRegistry<?> registry : registries.values()) {
            registry.clear();
        }
        registries.clear();
        LOGGER.info("All registries cleared");
    }

    /**
     * Gets the number of registries.
     *
     * @return Registry count
     */
    public int size() {
        return registries.size();
    }

    /**
     * Gets total number of entries across all registries.
     *
     * @return Total entry count
     */
    public int getTotalEntries() {
        return registries.values().stream()
            .mapToInt(SimpleRegistry::size)
            .sum();
    }

    /**
     * Prints registry statistics for debugging.
     */
    public void logStatistics() {
        LOGGER.info("=== Registry Statistics ===");
        LOGGER.info("Total Registries: " + registries.size());
        for (Map.Entry<String, SimpleRegistry<?>> entry : registries.entrySet()) {
            LOGGER.info("  " + entry.getKey() + ": " + entry.getValue().size() + " entries");
        }
        LOGGER.info("Total Entries: " + getTotalEntries());
    }
}
