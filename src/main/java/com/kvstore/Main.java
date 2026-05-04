package com.kvstore;

import com.kvstore.store.ExpiryManager;
import com.kvstore.store.StorageEngine;
import com.kvstore.server.HttpServerApp;

public class Main {
    public static void main(String[] args) throws Exception {
        StorageEngine engine = new StorageEngine();

        ExpiryManager expiryManager = new ExpiryManager(engine);
        expiryManager.start();

        new HttpServerApp(engine).start();
    }
}