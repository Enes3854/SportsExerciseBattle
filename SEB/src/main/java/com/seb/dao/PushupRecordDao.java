package com.seb.dao;

import com.seb.model.PushupRecord;
import com.seb.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PushupRecordDao {

    // Add a new push-up record
    public boolean addRecord(PushupRecord record) {
        final String query = "INSERT INTO pushup_records (count, user_id, duration, timestamp, tournament_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, record.getCount());
            stmt.setLong(2, record.getUserId());
            stmt.setLong(3, record.getDuration());
            stmt.setTimestamp(4, Timestamp.valueOf(record.getTimestamp()));
            stmt.setString(5, record.getTournamentId());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find records by user ID
    public List<PushupRecord> findRecordsByUserId(Long userId) {
        List<PushupRecord> records = new ArrayList<>();
        final String query = "SELECT * FROM pushup_records WHERE user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new PushupRecord(
                        rs.getLong("id"),
                        rs.getInt("count"),
                        userId,
                        rs.getLong("duration"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("tournament_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // Retrieves all push-up records for a specific tournament.
    public List<PushupRecord> findRecordsByTournamentId(String tournamentId) {
        List<PushupRecord> records = new ArrayList<>();
        final String query = "SELECT * FROM pushup_records WHERE tournament_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tournamentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new PushupRecord(
                        rs.getLong("id"),
                        rs.getInt("count"),
                        rs.getLong("user_id"),
                        rs.getLong("duration"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("tournament_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // Find all records
    public List<PushupRecord> findAllRecords() {
        List<PushupRecord> records = new ArrayList<>();
        final String query = "SELECT * FROM pushup_records";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new PushupRecord(
                        rs.getLong("id"),
                        rs.getInt("count"),
                        rs.getLong("user_id"),
                        rs.getLong("duration"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("tournament_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

}
