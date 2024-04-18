package com.seb.dao;

import com.seb.model.Tournament;
import com.seb.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TournamentDao {

    // Create a new tournament
    public boolean createTournament(Tournament tournament) {
        final String query = "INSERT INTO tournaments (id, start_time, end_time, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tournament.getId());
            stmt.setObject(2, tournament.getStartTime());
            stmt.setObject(3, tournament.getEndTime());
            stmt.setString(4, tournament.getStatus());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update tournament details
    public boolean updateTournament(Tournament tournament) {
        final String query = "UPDATE tournaments SET start_time = ?, end_time = ?, status = ?, winner_user_id = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, tournament.getStartTime());
            stmt.setObject(2, tournament.getEndTime());
            stmt.setString(3, tournament.getStatus());
            stmt.setLong(4, tournament.getWinnerUserId() == null ? Long.valueOf(0) : tournament.getWinnerUserId());
            stmt.setString(5, tournament.getId());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve a tournament by ID
    public Tournament findTournamentById(String id) {
        final String query = "SELECT * FROM tournaments WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tournament(
                        rs.getString("id"),
                        rs.getObject("start_time", LocalDateTime.class),
                        rs.getObject("end_time", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getLong("winner_user_id") == 0 ? null : rs.getLong("winner_user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Tournament findActiveTournament() {
        final String query = "SELECT * FROM tournaments WHERE end_time > NOW() AND status = 'Active' LIMIT 1";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Tournament(
                        rs.getString("id"),
                        rs.getObject("start_time", LocalDateTime.class),
                        rs.getObject("end_time", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getLong("winner_user_id") == 0 ? null : rs.getLong("winner_user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // List all tournaments
    public List<Tournament> findAllTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        final String query = "SELECT * FROM tournaments";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tournaments.add(new Tournament(
                        rs.getString("id"),
                        rs.getObject("start_time", LocalDateTime.class),
                        rs.getObject("end_time", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getLong("winner_user_id") == 0 ? null : rs.getLong("winner_user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }

    public List<Tournament> findActiveTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        final String query = "SELECT * FROM tournaments WHERE NOW() > end_time AND status = 'Active'";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tournaments.add(new Tournament(
                        rs.getString("id"),
                        rs.getObject("start_time", LocalDateTime.class),
                        rs.getObject("end_time", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getLong("winner_user_id") == 0 ? null : rs.getLong("winner_user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }
}
