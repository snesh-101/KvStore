package com.kvstore.store;

import java.util.concurrent.*;

public class ExpiryManager {

    private final StorageEngine engine;
    private final ScheduledExecutorService scheduler;

    public ExpiryManager(StorageEngine engine) {
        this.engine = engine;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        System.out.println("running");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS); // runs every 5 seconds
    }

    private void cleanup() {
        int removed = 0;

        for (String key : engine.getAllKeys()) {
            if (!engine.exists(key)) { // this will remove expired internally
                removed++;
            }
        }

        if (removed > 0) {
            System.out.println("Expired keys cleaned: " + removed);
        }
    }
}