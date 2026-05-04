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
        server.createContext("/", exchange -> {
            String response = """
                    <html>
                    <body>
                    <h2>KV Store API Running</h2>

                    <h3>Endpoints:</h3>

                    <b>SET:</b><br>
                    <code>/set?key=a&value=10</code><br><br>

                    <b>GET:</b><br>
                    <code>/get?key=a</code><br><br>

                    <b>DELETE:</b><br>
                    <code>/del?key=a</code><br><br>

                    </body>
                    </html>
                    """;

            byte[] responseBytes = response.getBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, responseBytes.length);
            exchange.getResponseBody().write(responseBytes);
            exchange.getResponseBody().close();
        });
        server.start();

        System.out.println("Server started on port " + port);
    }
}