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
import java.util.Map;

public class ScoreboardHandler implements HttpHandler {
    private final StatsService statsService = new StatsService();
    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScoreboardHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, "Unsupported method", 405, "Method Not Allowed");
            return;
        }

        // Authentication is checked but not fully implemented for demonstration
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        User user = HandlerUtils.isAuthorized(exchange, userService);
        if (user == null) {
            // Response already sent in isAuthorized
            return;
        }

        Map<String, Map<String, Object>> scoreboard = statsService.getScoreboard();
        String jsonResponse = objectMapper.writeValueAsString(scoreboard);

        sendResponse(exchange, jsonResponse, 200, "application/json");
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
