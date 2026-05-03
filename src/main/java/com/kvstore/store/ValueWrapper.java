package com.kvstore.store;

public class ValueWrapper {

    private final String value;
    private final long expiryTime; // -1 = no expiry

    public ValueWrapper(String value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        return expiryTime != -1 && System.currentTimeMillis() > expiryTime;
    }
}