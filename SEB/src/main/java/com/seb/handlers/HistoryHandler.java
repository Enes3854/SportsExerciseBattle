package com.seb.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.seb.model.PushupRecord;
import com.seb.model.User;
import com.seb.service.PushupService;
import com.seb.service.UserService;
import com.seb.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private final UserService userService = new UserService();
    private final PushupService pushupService = new PushupService();

    public HistoryHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGetRequest(exchange);
                break;
            case "POST":
                handlePostRequest(exchange);
                break;
            default:
                HandlerUtils.sendResponse(exchange, "Unsupported method", 405, "application/json");
                break;
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String username = HandlerUtils.getUsernameFromAuthHeader(exchange.getRequestHeaders().getFirst("Authorization"));
        if (username == null) {
            HandlerUtils.sendResponse(exchange, "Unauthorized", 401, "application/json");
            return;
        }

        Long userId = userService.getUserIdByUsername(username);
        if (userId == null) {
            HandlerUtils.sendResponse(exchange, "User not found", 404, "application/json");
            return;
        }

        List<PushupRecord> history = pushupService.getRecordsForUser(userId);
        String response = JsonUtil.toJson(history);

        HandlerUtils.sendResponse(exchange, response, 200, "application/json");
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String username = HandlerUtils.getUsernameFromAuthHeader(exchange.getRequestHeaders().getFirst("Authorization"));
        if (username == null) {
            HandlerUtils.sendResponse(exchange, "Unauthorized", 401, "application/json");
            return;
        }

        Long userId = userService.getUserIdByUsername(username);
        if (userId == null) {
            HandlerUtils.sendResponse(exchange, "User not found", 404, "application/json");
            return;
        }

        JsonNode requestBody = HandlerUtils.readJsonBody(exchange);
        if (requestBody == null || !requestBody.has("Count") || !requestBody.has("DurationInSeconds")) {
            HandlerUtils.sendResponse(exchange, "Invalid request body", 400, "application/json");
            return;
        }

        int count = requestBody.get("Count").asInt();
        long durationInSeconds = requestBody.get("DurationInSeconds").asLong();
        LocalDateTime timestamp = LocalDateTime.now();

        boolean success = pushupService.addPushupRecord(count, userId, durationInSeconds, timestamp);

        if (success) {
            HandlerUtils.sendResponse(exchange, "Push-up record added successfully", 200, "application/json");
        } else {
            HandlerUtils.sendResponse(exchange, "Failed to add push-up record", 500, "application/json");
        }
    }
}
