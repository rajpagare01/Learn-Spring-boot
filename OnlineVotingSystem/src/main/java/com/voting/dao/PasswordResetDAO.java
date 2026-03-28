package com.voting.dao;

import com.voting.util.DBConnection;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordResetDAO {

    private static final Logger LOGGER = Logger.getLogger(PasswordResetDAO.class.getName());

    public void invalidateForUser(int userId) {
        String sql = "UPDATE password_reset_tokens SET used = 1 WHERE user_id = ? AND used = 0";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "invalidate reset tokens", e);
        }
    }

    public void saveToken(int userId, String token) {
        invalidateForUser(userId);
        Timestamp exp = Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES));
        String sql = "INSERT INTO password_reset_tokens (user_id, token, expires_at) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, exp);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "save reset token", e);
        }
    }

    public Integer findValidUserId(String token) {
        String sql = "SELECT user_id FROM password_reset_tokens WHERE token = ? AND used = 0 AND expires_at > NOW()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findValidUserId", e);
        }
        return null;
    }

    public void markUsed(String token) {
        String sql = "UPDATE password_reset_tokens SET used = 1 WHERE token = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "markUsed", e);
        }
    }
}
