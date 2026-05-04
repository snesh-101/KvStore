package com.kvstore.server;

import com.kvstore.store.StorageEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class GetHandler implements HttpHandler {

    private final StorageEngine engine;

    public GetHandler(StorageEngine engine) {
        this.engine = engine;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = QueryParser.parse(exchange.getRequestURI().getQuery());

        System.out.println("Thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String key = params.get("key");
        String value = engine.get(key);

        String response = value == null ? "null" : value;

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}