package io.julia0x.atlas.core.impl.cache;

/**
 * Represents a single cache entry with metadata.
 */
public class CacheEntry {
    private final Object value;
    private final long createdAt;
    private final long ttl; // TTL in milliseconds, 0 or negative means no expiration
    private long lastAccessedAt;
    private long accessCount;

    /**
     * Creates a new cache entry.
     *
     * @param value The cached value
     * @param ttl The time-to-live in milliseconds (0 or negative for no expiration)
     */
    public CacheEntry(Object value, long ttl) {
        this.value = value;
        this.createdAt = System.currentTimeMillis();
        this.lastAccessedAt = createdAt;
        this.ttl = ttl;
        this.accessCount = 0;
    }

    /**
     * Checks if this entry has expired.
     *
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        if (ttl <= 0) {
            return false; // No expiration
        }
        return System.currentTimeMillis() - createdAt > ttl;
    }

    /**
     * Updates the last accessed timestamp.
     */
    public void updateAccess() {
        this.lastAccessedAt = System.currentTimeMillis();
        this.accessCount++;
    }

    /**
     * Gets the cached value.
     *
     * @return The value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return Creation time in milliseconds
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the TTL in milliseconds.
     *
     * @return TTL value
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * Gets the last accessed timestamp.
     *
     * @return Last access time in milliseconds
     */
    public long getLastAccessedAt() {
        return lastAccessedAt;
    }

    /**
     * Gets the number of times this entry has been accessed.
     *
     * @return Access count
     */
    public long getAccessCount() {
        return accessCount;
    }

    /**
     * Gets the time remaining before expiration.
     *
     * @return Time remaining in milliseconds, or -1 if no expiration
     */
    public long getTimeToExpire() {
        if (ttl <= 0) {
            return -1;
        }
        return ttl - (System.currentTimeMillis() - createdAt);
    }

    /**
     * Gets the age of this entry.
     *
     * @return Age in milliseconds
     */
    public long getAge() {
        return System.currentTimeMillis() - createdAt;
    }
}
