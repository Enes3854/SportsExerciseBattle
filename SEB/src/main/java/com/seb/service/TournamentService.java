package com.seb.service;

import com.seb.dao.TournamentDao;
import com.seb.model.Tournament;

import java.time.LocalDateTime;
import java.util.List;

public class TournamentService {
    private TournamentDao tournamentDao = new TournamentDao();

    public TournamentService() {
    }

    public TournamentService(TournamentDao tournamentDaoMock) {
        this.tournamentDao = tournamentDaoMock;
    }

    public Tournament findActiveTournament() {
        return tournamentDao.findActiveTournament();
    }

    public List<Tournament> findActiveTournaments() {
        return tournamentDao.findActiveTournaments();
    }

    public boolean createTournament(Tournament tournament) {
        return tournamentDao.createTournament(tournament);
    }

    public boolean updateTournament(String tournamentId, LocalDateTime startTime, LocalDateTime endTime, String status, Long winnerUserId) {
        Tournament tournament = new Tournament(tournamentId, startTime, endTime, status, winnerUserId);
        return tournamentDao.updateTournament(tournament);
    }

    public boolean updateTournament(Tournament tournament) {
        return tournamentDao.updateTournament(tournament);
    }

    public List<Tournament> findAllTournaments() {
        return tournamentDao.findAllTournaments();
    }

}
