package com.kvstore.server;

import com.kvstore.store.StorageEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class SetHandler implements HttpHandler {

    private final StorageEngine engine;

    public SetHandler(StorageEngine engine) {
        this.engine = engine;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> params = QueryParser.parse(exchange.getRequestURI().getQuery());

        String key = params.get("key");
        String value = params.get("value");

        // 🔥 TTL handling
        long ttl = -1;
        if (params.containsKey("ttl")) {
            try {
                ttl = Long.parseLong(params.get("ttl"));
            } catch (NumberFormatException e) {
                ttl = -1;
            }
        }

        if (key == null || value == null) {
            String response = "Missing key or value";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        engine.put(key, value, ttl);

        String response = "OK";
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}