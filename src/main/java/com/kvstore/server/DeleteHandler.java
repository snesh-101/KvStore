package com.kvstore.server;

import com.kvstore.store.StorageEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class DeleteHandler implements HttpHandler {

    private final StorageEngine engine;

    public DeleteHandler(StorageEngine engine) {
        this.engine = engine;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = QueryParser.parse(exchange.getRequestURI().getQuery());

        String key = params.get("key");
        engine.delete(key);

        String response = "DELETED";
        exchange.sendResponseHeaders(200, response.length());

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}