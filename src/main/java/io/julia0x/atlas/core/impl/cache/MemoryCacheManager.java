package io.julia0x.atlas.core.impl.cache;

import io.julia0x.atlas.core.api.cache.ICacheManager;
import io.julia0x.atlas.core.utils.logging.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * In-memory cache manager with TTL and LRU eviction support.
 */
public class MemoryCacheManager implements ICacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCacheManager.class);
    private static final int DEFAULT_MAX_SIZE = 1000;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final int maxSize;
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;

    /**
     * Creates a new cache manager with default max size.
     */
    public MemoryCacheManager() {
        this(DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a new cache manager with specified max size.
     *
     * @param maxSize The maximum number of entries to cache
     */
    public MemoryCacheManager(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void put(String key, Object value, long ttlMillis) {
        if (key == null || key.isEmpty()) {
            LOGGER.warning("Attempted to cache with empty key");
            return;
        }

        if (value == null) {
            remove(key);
            return;
        }

        // Check if we need to evict
        if (cache.size() >= maxSize && !cache.containsKey(key)) {
            evictLRU();
        }

        cache.put(key, new CacheEntry(value, ttlMillis));
        LOGGER.fine("Cached '" + key + "' with TTL: " + ttlMillis + "ms");
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, 0); // No expiration
    }

    @Override
    public Optional<Object> get(String key) {
        CacheEntry entry = cache.get(key);

        if (entry == null) {
            misses++;
            return Optional.empty();
        }

        if (entry.isExpired()) {
            cache.remove(key);
            misses++;
            LOGGER.fine("Cache entry '" + key + "' expired");
            return Optional.empty();
        }

        entry.updateAccess();
        hits++;
        return Optional.of(entry.getValue());
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        Optional<Object> value = get(key);
        if (value.isPresent() && type.isInstance(value.get())) {
            return Optional.of(type.cast(value.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean remove(String key) {
        return cache.remove(key) != null;
    }

    @Override
    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return entry != null;
    }

    @Override
    public void clear() {
        cache.clear();
        hits = 0;
        misses = 0;
        evictions = 0;
        LOGGER.info("Cache cleared");
    }

    @Override
    public int size() {
        cleanup();
        return cache.size();
    }

    @Override
    public void cleanup() {
        List<String> expired = new ArrayList<>();
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                expired.add(entry.getKey());
            }
        }

        for (String key : expired) {
            cache.remove(key);
            LOGGER.fine("Cleaned up expired cache entry: " + key);
        }
    }

    @Override
    public CacheStats getStats() {
        cleanup();
        return new CacheStats() {
            @Override
            public long getHits() {
                return hits;
            }

            @Override
            public long getMisses() {
                return misses;
            }

            @Override
            public long getEvictions() {
                return evictions;
            }

            @Override
            public double getHitRate() {
                long total = hits + misses;
                return total == 0 ? 0.0 : (double) hits / total * 100;
            }
        };
    }

    private void evictLRU() {
        String lruKey = null;
        long lruTime = Long.MAX_VALUE;

        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().getLastAccessedAt() < lruTime) {
                lruTime = entry.getValue().getLastAccessedAt();
                lruKey = entry.getKey();
            }
        }

        if (lruKey != null) {
            cache.remove(lruKey);
            evictions++;
            LOGGER.fine("Evicted LRU cache entry: " + lruKey);
        }
    }

    /**
     * Logs cache statistics for debugging.
     */
    public void logStatistics() {
        CacheStats stats = getStats();
        LOGGER.info("=== Cache Statistics ===");
        LOGGER.info("Size: " + size() + "/" + maxSize);
        LOGGER.info("Hits: " + stats.getHits());
        LOGGER.info("Misses: " + stats.getMisses());
        LOGGER.info("Hit Rate: " + String.format("%.2f%%", stats.getHitRate()));
        LOGGER.info("Evictions: " + stats.getEvictions());
    }
}
