package com.voting.dao;

import com.voting.util.DBConnection;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailVerifyTokenDAO {

    private static final Logger LOGGER = Logger.getLogger(EmailVerifyTokenDAO.class.getName());

    public void save(int userId, String token) {
        deleteForUser(userId);
        Timestamp exp = Timestamp.from(Instant.now().plus(48, ChronoUnit.HOURS));
        String sql = "INSERT INTO email_verify_tokens (user_id, token, expires_at) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, exp);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "save email verify", e);
        }
    }

    public void deleteForUser(int userId) {
        String sql = "DELETE FROM email_verify_tokens WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "delete email verify", e);
        }
    }

    public Integer findValidUserId(String token) {
        String sql = "SELECT user_id FROM email_verify_tokens WHERE token = ? AND expires_at > NOW()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findValidUserId email", e);
        }
        return null;
    }

    public void deleteByToken(String token) {
        String sql = "DELETE FROM email_verify_tokens WHERE token = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteByToken", e);
        }
    }
}
