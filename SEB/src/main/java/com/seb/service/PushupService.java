package com.seb.service;

import com.seb.dao.PushupRecordDao;
import com.seb.model.PushupRecord;
import com.seb.model.Tournament;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.UUID;

public class PushupService {
    private PushupRecordDao pushupRecordDao = new PushupRecordDao();
    private TournamentService tournamentService = new TournamentService();

    public PushupService() {
    }

    public PushupService(PushupRecordDao pushupRecordDaoMock, TournamentService tournamentServiceMock) {
        this.pushupRecordDao = pushupRecordDaoMock;
        this.tournamentService = tournamentServiceMock;
    }

    public boolean addPushupRecord(int count, Long userId, long duration, LocalDateTime timestamp) {
        // Find an active tournament or create a new one if none exists.
        Tournament activeTournament = tournamentService.findActiveTournament();
        if (activeTournament == null) {
            activeTournament = createNewTournament(timestamp);
        }

        PushupRecord record = new PushupRecord(null, count, userId, duration, timestamp, activeTournament.getId());
        return pushupRecordDao.addRecord(record);
    }

    public List<Map<String, Object>> getDetailedRecordsForTournament(String tournamentId) {
        List<PushupRecord> records = pushupRecordDao.findRecordsByTournamentId(tournamentId);
        return records.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getCount(), r1.getCount())) // Sort descending by count
                .map(r -> Map.<String, Object>of(
                        "UserId", r.getUserId(),
                        "Count", r.getCount(),
                        "Duration", r.getDuration(),
                        "Timestamp", r.getTimestamp().toString()))
                .collect(Collectors.toList());
    }

    private Tournament createNewTournament(LocalDateTime timestamp) {
        String tournamentId = UUID.randomUUID().toString();
        Tournament newTournament = new Tournament(tournamentId, timestamp, timestamp.plusMinutes(2), "Active", null);
        tournamentService.createTournament(newTournament);
        return newTournament;
    }

    public List<PushupRecord> getRecordsForUser(Long userId) {
        return pushupRecordDao.findRecordsByUserId(userId);
    }

    public List<PushupRecord> getRecordsForTournament(String tournamentId) {
        return pushupRecordDao.findRecordsByTournamentId(tournamentId);
    }

    public List<PushupRecord> getAllRecords() {
        return pushupRecordDao.findAllRecords();
    }
}
