package com.seb.server;

import com.seb.handlers.*;
import com.seb.service.TournamentCoordinator;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerSetup {

    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on port 10001
        HttpServer server = HttpServer.create(new InetSocketAddress(10001), 0);

        // Register handlers for each path directly without intermediate variables
        server.createContext("/user", new UserHandler());
        server.createContext("/sessions", new UserHandler());
        server.createContext("/pushup", new PushupHandler());
        server.createContext("/stats", new StatsHandler());
        server.createContext("/score", new ScoreboardHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/tournament", new TournamentHandler());

        // Set the default executor
        server.setExecutor(null);

        TournamentCoordinator coordinator = new TournamentCoordinator();
        coordinator.start();

        // Start the server
        server.start();
        System.out.println("Server started on port 10001.");
    }
}
