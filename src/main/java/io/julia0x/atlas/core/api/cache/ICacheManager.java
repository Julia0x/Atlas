package io.julia0x.atlas.core.api.cache;

import java.util.Optional;

/**
 * Manages caching of objects with optional TTL (Time To Live) support.
 */
public interface ICacheManager {
    /**
     * Stores an object in the cache with the given key.
     *
     * @param key The cache key
     * @param value The value to cache
     * @param ttlMillis The time-to-live in milliseconds (0 or negative for no expiration)
     */
    void put(String key, Object value, long ttlMillis);

    /**
     * Stores an object in the cache without expiration.
     *
     * @param key The cache key
     * @param value The value to cache
     */
    void put(String key, Object value);

    /**
     * Retrieves a cached value by key.
     *
     * @param key The cache key
     * @return An Optional containing the cached value, or empty if not found or expired
     */
    Optional<Object> get(String key);

    /**
     * Retrieves a cached value by key with type safety.
     *
     * @param key The cache key
     * @param type The expected type
     * @param <T> The value type
     * @return An Optional containing the cached value, or empty if not found or expired
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Removes a value from the cache.
     *
     * @param key The cache key
     * @return true if the key was found and removed, false otherwise
     */
    boolean remove(String key);

    /**
     * Checks if a key exists in the cache and has not expired.
     *
     * @param key The cache key
     * @return true if the key exists and is valid, false otherwise
     */
    boolean contains(String key);

    /**
     * Clears all cached values.
     */
    void clear();

    /**
     * Gets the number of valid entries in the cache.
     *
     * @return The count of cached entries
     */
    int size();

    /**
     * Performs cache cleanup, removing expired entries.
     * This is typically called periodically by the manager.
     */
    void cleanup();

    /**
     * Gets statistics about cache performance.
     *
     * @return Cache statistics object
     */
    CacheStats getStats();

    /**
     * Statistics about cache performance.
     */
    interface CacheStats {
        long getHits();
        long getMisses();
        long getEvictions();
        double getHitRate();
    }
}
