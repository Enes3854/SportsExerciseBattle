package com.seb.dao;

import com.seb.model.User;
import com.seb.util.DbUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Finds user by username
    public User findUserByUsername(String username) {
        final String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Creates a new user
    public boolean createUser(User user) {
        final String query = "INSERT INTO users (username, password, elo_score, name, bio, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Consider hashing the password before calling this method
            stmt.setInt(3, user.getEloScore());
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getBio());
            stmt.setString(6, user.getImage());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Updates user details
    public boolean updateUserDetails(User user) {
        final String query = "UPDATE users SET name = ?, bio = ?, image = ?, password = ?, elo_score = ? WHERE username = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getBio());
            stmt.setString(3, user.getImage());
            stmt.setString(4, user.getPassword()); // Ensure this is hashed if updating password
            stmt.setInt(5, user.getEloScore());
            stmt.setString(6, user.getUsername());

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all users
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        final String query = "SELECT * FROM users";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Utility method to extract user data from ResultSet
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("elo_score"),
                rs.getString("name"),
                rs.getString("bio"),
                rs.getString("image")
        );
    }

    public User findUserById(Long userId) {
        final String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
