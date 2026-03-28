package com.voting.dao;

import com.voting.model.Election;
import com.voting.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ElectionDAO — CRUD operations for the elections table.
 */
public class ElectionDAO {

    private static final Logger LOGGER = Logger.getLogger(ElectionDAO.class.getName());

    // ── Create ────────────────────────────────────────────

    public boolean createElection(Election e) {
        String sql = "INSERT INTO elections (title, description, start_date, end_date, status, created_by) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDescription());
            ps.setTimestamp(3, e.getStartDate());
            ps.setTimestamp(4, e.getEndDate());
            ps.setString(5, e.getStatus());
            ps.setInt(6, e.getCreatedBy());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) e.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "createElection failed", ex);
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────

    public Election findById(int id) {
        String sql = "SELECT * FROM elections WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findById failed: " + id, e);
        }
        return null;
    }

    public List<Election> findAll() {
        return findByStatus(null);
    }

    public List<Election> findActiveElections() {
        return findByStatus("ACTIVE");
    }

    public List<Election> findClosedElections() {
        return findByStatus("CLOSED");
    }

    public List<Election> findUpcomingElections() {
        return findByStatus("UPCOMING");
    }

    private List<Election> findByStatus(String status) {
        List<Election> list = new ArrayList<>();
        String sql = (status == null)
            ? "SELECT * FROM elections ORDER BY start_date DESC"
            : "SELECT * FROM elections WHERE status = ? ORDER BY start_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (status != null) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByStatus failed: " + status, e);
        }
        return list;
    }

    // ── Update ────────────────────────────────────────────

    public boolean updateElection(Election e) {
        String sql = "UPDATE elections SET title=?, description=?, start_date=?, "
                   + "end_date=?, status=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getTitle());
            ps.setString(2, e.getDescription());
            ps.setTimestamp(3, e.getStartDate());
            ps.setTimestamp(4, e.getEndDate());
            ps.setString(5, e.getStatus());
            ps.setInt(6, e.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "updateElection failed: " + e.getId(), ex);
        }
        return false;
    }

    public boolean updateStatus(int electionId, String newStatus) {
        String sql = "UPDATE elections SET status=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, electionId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "updateStatus failed", e);
        }
        return false;
    }

    // ── Delete ────────────────────────────────────────────

    public boolean deleteElection(int id) {
        // Cascades to candidates and votes via FK ON DELETE CASCADE
        String sql = "DELETE FROM elections WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteElection failed: " + id, e);
        }
        return false;
    }

    // ── Auto-sync status from dates (call on app startup / scheduler) ──

    /**
     * Updates election status based on current datetime:
     *   now < start_date  → UPCOMING
     *   now between dates → ACTIVE
     *   now > end_date    → CLOSED
     */
    public void syncElectionStatuses() {
        String sql = "UPDATE elections SET status = "
                   + "CASE "
                   + "  WHEN NOW() < start_date THEN 'UPCOMING' "
                   + "  WHEN NOW() BETWEEN start_date AND end_date THEN 'ACTIVE' "
                   + "  ELSE 'CLOSED' "
                   + "END";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int updated = ps.executeUpdate();
            LOGGER.info("syncElectionStatuses: updated " + updated + " rows.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "syncElectionStatuses failed", e);
        }
    }

    // ── Counts ────────────────────────────────────────────

    public int countAll() {
        return countByStatus(null);
    }

    public int countByStatus(String status) {
        String sql = (status == null)
            ? "SELECT COUNT(*) FROM elections"
            : "SELECT COUNT(*) FROM elections WHERE status = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (status != null) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countByStatus failed", e);
        }
        return 0;
    }

    // ── Row mapper ────────────────────────────────────────

    private Election mapRow(ResultSet rs) throws SQLException {
        Election e = new Election();
        e.setId(rs.getInt("id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setStartDate(rs.getTimestamp("start_date"));
        e.setEndDate(rs.getTimestamp("end_date"));
        e.setStatus(rs.getString("status"));
        e.setCreatedBy(rs.getInt("created_by"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        return e;
    }
}
