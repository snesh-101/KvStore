package com.kvstore.server;

import com.kvstore.store.StorageEngine;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServerApp {

    private final StorageEngine engine;

    public HttpServerApp(StorageEngine engine) {
        this.engine = engine;
    }

    public void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/set", new SetHandler(engine));
        server.createContext("/get", new GetHandler(engine));
        server.createContext("/del", new DeleteHandler(engine));

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Server started on port " + port);
    }
}