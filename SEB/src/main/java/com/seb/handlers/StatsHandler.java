package com.seb.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb.model.User;
import com.seb.service.StatsService;
import com.seb.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StatsHandler implements HttpHandler {
    private final UserService userService = new UserService();
    private final StatsService statsService = new StatsService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatsHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, "Unsupported method", 405, "Method Not Allowed");
            return;
        }

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            sendResponse(exchange, "Authorization header is missing or invalid", 401, "Unauthorized");
            return;
        }

        String username = HandlerUtils.getUsernameFromAuthHeader(authHeader);
        if (username == null || !userService.isValidUser(authHeader, username)) {
            sendResponse(exchange, "Unauthorized", 401, "Unauthorized");
            return;
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            sendResponse(exchange, "User not found", 404, "Not Found");
            return;
        }

        // Gather stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("eloScore", user.getEloScore());
        stats.put("pushupCount", statsService.getTotalPushupsForUser(user.getId()));

        String jsonResponse = objectMapper.writeValueAsString(stats);
        sendResponse(exchange, jsonResponse, 200, "application/json");
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode, String reasonPhrase) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
