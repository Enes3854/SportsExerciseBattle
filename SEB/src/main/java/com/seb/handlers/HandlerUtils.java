package com.seb.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.seb.model.User;
import com.seb.service.UserService;
import com.seb.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HandlerUtils {

    public static void sendResponse(HttpExchange exchange, String response, int statusCode, String contentType) throws IOException {
        if (contentType != null && !contentType.isEmpty()) {
            exchange.getResponseHeaders().set("Content-Type", contentType);
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static JsonNode readJsonBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return JsonUtil.fromJson(requestBody, JsonNode.class);
    }

    public static String getUsernameFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return null;
        }
        String credentials = authHeader.substring("Basic ".length()).trim();
        return credentials.split("-sebToken", 2)[0];
    }

    public static User isAuthorized(HttpExchange exchange, UserService userService) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        String username = getUsernameFromAuthHeader(authHeader);
        if (username == null) {
            sendResponse(exchange, "Unauthorized", 401, "application/json");
            return null;
        }

        User user = userService.getUserByUsername(username);
        if (user == null || !userService.isValidUser(authHeader, username)) {
            sendResponse(exchange, "Unauthorized", 401, "application/json");
            return null;
        }
        return user; // Return the user object instead of a boolean
    }
}
