package com.seb.service;

import com.seb.model.PushupRecord;
import com.seb.model.Tournament;
import com.seb.dao.TournamentDao;
import com.seb.dao.PushupRecordDao;
import com.seb.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TournamentCoordinator {
    private final TournamentService tournamentService = new TournamentService();
    private final PushupRecordDao pushupRecordDao = new PushupRecordDao();
    private final UserService userService = new UserService();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TournamentCoordinator() {
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateTournaments, 0, 2, TimeUnit.MINUTES);
    }

    private void checkAndUpdateTournaments() {
        List<Tournament> activeTournaments = tournamentService.findActiveTournaments();
        for (Tournament tournament : activeTournaments) {
            if (tournament.getEndTime().isBefore(LocalDateTime.now()) && "Active".equals(tournament.getStatus())) {
                // Tournament has finished, determine winner, update status, and update user ELOs
                updateTournamentStatus(tournament);
                updateParticipantsELO(tournament);
            }
        }
    }

    private void updateTournamentStatus(Tournament tournament) {
        List<PushupRecord> records = pushupRecordDao.findRecordsByTournamentId(tournament.getId());
        Long winnerUserId = determineWinner(records);
        tournament.setWinnerUserId(winnerUserId);
        tournament.setStatus("Completed");
        tournamentService.updateTournament(tournament);
    }

    private Long determineWinner(List<PushupRecord> records) {
        Map<Long, Integer> scores = records.stream()
                .collect(Collectors.groupingBy(PushupRecord::getUserId, Collectors.summingInt(PushupRecord::getCount)));
        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private void updateParticipantsELO(Tournament tournament) {
        List<PushupRecord> records = pushupRecordDao.findRecordsByTournamentId(tournament.getId());
        Map<Long, Integer> scores = records.stream()
                .collect(Collectors.groupingBy(PushupRecord::getUserId, Collectors.summingInt(PushupRecord::getCount)));
        Long winnerUserId = tournament.getWinnerUserId();
        scores.forEach((userId, score) -> {
            User user = userService.getUserById(userId);
            if (user != null) {
                if (userId.equals(winnerUserId)) {
                    user.setEloScore(user.getEloScore() + 2);
                } else {
                    user.setEloScore(user.getEloScore() - 1);
                }
                userService.updateUser(user);
            }
        });
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
