package com.voting.dao;

import com.voting.util.DBConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuditLogDAO {

    private static final Logger LOGGER = Logger.getLogger(AuditLogDAO.class.getName());

    public void insert(String eventType, Integer userId, String detail, String ip) {
        String sql = "INSERT INTO audit_log (event_type, user_id, detail, ip_address) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventType);
            if (userId == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, userId);
            ps.setString(3, detail);
            ps.setString(4, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "audit insert failed: " + eventType, e);
        }
    }
}
