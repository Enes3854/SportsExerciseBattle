package com.seb.handlers;

import com.seb.model.Tournament;
import com.seb.model.User;
import com.seb.service.TournamentService;
import com.seb.service.UserService;
import com.seb.service.PushupService;
import com.seb.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TournamentHandler implements HttpHandler {
    private final UserService userService = new UserService();
    private final TournamentService tournamentService = new TournamentService();
    private final PushupService pushupService = new PushupService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            HandlerUtils.sendResponse(exchange, "Unsupported method", 405, "application/json");
            return;
        }

        String username = HandlerUtils.getUsernameFromAuthHeader(exchange.getRequestHeaders().getFirst("Authorization"));
        if (username == null) {
            HandlerUtils.sendResponse(exchange, "Unauthorized", 401, "application/json");
            return;
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            HandlerUtils.sendResponse(exchange, "User not found", 404, "application/json");
            return;
        }

        List<Tournament> tournaments = tournamentService.findAllTournaments();
        List<Map<String, Object>> tournamentDetails = tournaments.stream()
                .map(tournament -> {
                    List<Map<String, Object>> records = pushupService.getDetailedRecordsForTournament(tournament.getId());
                    return Map.<String, Object>of(
                            "Tournament", tournament,
                            "Records", records
                    );
                })
                .collect(Collectors.toList());

        if (!tournamentDetails.isEmpty()) {
            String response = JsonUtil.toJson(Map.of("Tournaments", tournamentDetails));
            HandlerUtils.sendResponse(exchange, response, 200, "application/json");
        } else {
            HandlerUtils.sendResponse(exchange, "No active tournaments", 404, "application/json");
        }
    }
}
