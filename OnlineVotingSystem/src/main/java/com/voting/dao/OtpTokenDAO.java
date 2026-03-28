package com.voting.dao;

import com.voting.util.DBConnection;
import com.voting.util.ReceiptUtil;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OtpTokenDAO {

    private static final Logger LOGGER = Logger.getLogger(OtpTokenDAO.class.getName());

    public void deleteForUser(int userId) {
        String sql = "DELETE FROM otp_tokens WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "deleteForUser otp", e);
        }
    }

    public void save(int userId, String plainOtp) {
        deleteForUser(userId);
        String hash = ReceiptUtil.sha256Hex(
            com.voting.util.AppConfig.receiptSecret() + "|" + plainOtp + "|" + userId);
        Timestamp exp = Timestamp.from(Instant.now().plus(5, ChronoUnit.MINUTES));
        String sql = "INSERT INTO otp_tokens (user_id, code_hash, expires_at) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, hash);
            ps.setTimestamp(3, exp);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "save otp", e);
        }
    }

    public boolean verifyAndConsume(int userId, String plainOtp) {
        String hash = ReceiptUtil.sha256Hex(
            com.voting.util.AppConfig.receiptSecret() + "|" + plainOtp + "|" + userId);
        String sql = "DELETE FROM otp_tokens WHERE user_id = ? AND code_hash = ? AND expires_at > NOW()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, hash);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "verify otp", e);
            return false;
        }
    }
}
