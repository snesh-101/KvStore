package com.kvstore.store;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StorageEngine {

    private final ConcurrentHashMap<String, ValueWrapper> store;

    public StorageEngine() {
        this.store = new ConcurrentHashMap<>();
    }

    /**
     * Put key with optional TTL (in milliseconds)
     * ttl <= 0 means no expiry
     */
    public void put(String key, String value, long ttlMillis) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or Value cannot be null");
        }

        long expiryTime = (ttlMillis > 0)
                ? System.currentTimeMillis() + ttlMillis
                : -1;

        ValueWrapper wrapper = new ValueWrapper(value, expiryTime);
        store.put(key, wrapper);
    }

    /**
     * Get value for key
     * Returns null if key doesn't exist or expired
     */
    public String get(String key) {
        if (key == null)
            return null;

        ValueWrapper wrapper = store.get(key);
        if (wrapper == null)
            return null;

        // Lazy expiration check
        if (wrapper.isExpired()) {
            store.remove(key);
            return null;
        }

        return wrapper.getValue();
    }

    /**
     * Delete key
     */
    public boolean delete(String key) {
        if (key == null)
            return false;
        return store.remove(key) != null;
    }

    /**
     * Check if key exists and not expired
     */
    public boolean exists(String key) {
        if (key == null)
            return false;

        ValueWrapper wrapper = store.get(key);
        if (wrapper == null)
            return false;

        if (wrapper.isExpired()) {
            store.remove(key);
            return false;
        }

        return true;
    }

    /**
     * Get total number of keys (includes expired until accessed)
     */
    public int size() {
        return store.size();
    }

    /**
     * Clear entire store
     */
    public void clear() {
        store.clear();
    }

    public Set<String> getAllKeys() {
        return store.keySet();
    }
}