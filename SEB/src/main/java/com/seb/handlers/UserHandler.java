package com.seb.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.seb.model.User;
import com.seb.service.UserService;
import com.seb.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class UserHandler implements HttpHandler {

    private final UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(exchange);
                break;
            case "POST":
                handlePostRequest(exchange);
                break;
            case "PUT":
                handlePutRequest(exchange);
                break;
            default:
                HandlerUtils.sendResponse(exchange, "{\"message\":\"Unsupported method\"}", 405, "application/json");
                break;
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches("/users/\\w+")) {
            String username = path.substring(path.lastIndexOf('/') + 1);
            User authenticatedUser = HandlerUtils.isAuthorized(exchange, userService);
            if (authenticatedUser != null && authenticatedUser.getUsername().equals(username)) {
                HandlerUtils.sendResponse(exchange, JsonUtil.toJson(authenticatedUser), 200, "application/json");
            } else {
                HandlerUtils.sendResponse(exchange, "{\"message\":\"Unauthorized access or User not found\"}", 401, "application/json");
            }
        } else {
            HandlerUtils.sendResponse(exchange, "{\"message\":\"Invalid request\"}", 400, "application/json");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        JsonNode requestBody = HandlerUtils.readJsonBody(exchange);
        if ("/users".equals(exchange.getRequestURI().getPath())) {
            registerUser(requestBody, exchange);
        } else if ("/sessions".equals(exchange.getRequestURI().getPath())) {
            loginUser(requestBody, exchange);
        } else {
            HandlerUtils.sendResponse(exchange, "{\"message\":\"Invalid request\"}", 400, "application/json");
        }
    }

    private void registerUser(JsonNode requestBody, HttpExchange exchange) throws IOException {
        String username = requestBody.get("Username").asText();
        String password = requestBody.get("Password").asText(); // Should hash the password
        boolean success = userService.registerUser(username, password);
        String responseMessage = success ? "User registered successfully" : "Registration failed (user may already exist)";
        HandlerUtils.sendResponse(exchange, "{\"message\":\"" + responseMessage + "\"}", success ? 200 : 400, "application/json");
    }

    private void loginUser(JsonNode requestBody, HttpExchange exchange) throws IOException {
        String username = requestBody.get("Username").asText();
        String password = requestBody.get("Password").asText();
        User user = userService.loginUser(username, password);
        String responseMessage = user != null ? "Login successful" : "Login failed (invalid credentials)";
        HandlerUtils.sendResponse(exchange, "{\"message\":\"" + responseMessage + "\"}", user != null ? 200 : 401, "application/json");
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.matches("/users/\\w+")) {
            String usernamePath = path.substring(path.lastIndexOf('/') + 1);
            User authenticatedUser = HandlerUtils.isAuthorized(exchange, userService);
            if (authenticatedUser != null && authenticatedUser.getUsername().equals(usernamePath)) {
                updateUserDetails(exchange, authenticatedUser);
            } else {
                HandlerUtils.sendResponse(exchange, "{\"message\":\"Unauthorized access\"}", 401, "application/json");
            }
        } else {
            HandlerUtils.sendResponse(exchange, "{\"message\":\"Invalid request\"}", 400, "application/json");
        }
    }

    private void updateUserDetails(HttpExchange exchange, User user) throws IOException {
        JsonNode requestBody = HandlerUtils.readJsonBody(exchange);
        String name = requestBody.has("Name") ? requestBody.get("Name").asText() : user.getName();
        String bio = requestBody.has("Bio") ? requestBody.get("Bio").asText() : user.getBio();
        String image = requestBody.has("Image") ? requestBody.get("Image").asText() : user.getImage();
        user.setName(name);
        user.setBio(bio);
        user.setImage(image);
        boolean success = userService.updateUser(user);
        String responseMessage = success ? "User updated successfully." : "Failed to update user.";
        HandlerUtils.sendResponse(exchange, "{\"message\":\"" + responseMessage + "\"}", success ? 200 : 400, "application/json");
    }
}
