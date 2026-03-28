package com.voting.dao;

import com.voting.model.User;
import com.voting.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserDAO — all database operations for the users table.
 * Uses PreparedStatement throughout to prevent SQL injection.
 */
public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    // ── Register ──────────────────────────────────────────

    /**
     * Inserts a new VOTER user. Password is BCrypt-hashed before storage.
     *
     * @return true if insert succeeded
     */
    public boolean registerUser(String name, String email, String plainPassword) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'VOTER')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashed);

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.warning("Duplicate email on register: " + email);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "registerUser failed", e);
            return false;
        }
    }

    // ── Login ─────────────────────────────────────────────

    /**
     * Validates credentials.
     *
     * @return the User if credentials match, null otherwise
     */
    public User loginUser(String email, String plainPassword) {
        String sql = "SELECT * FROM users WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (passwordMatches(plainPassword, storedHash)) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "loginUser failed for: " + email, e);
        }
        return null;
    }

    // ── Finders ───────────────────────────────────────────

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
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

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByEmail failed: " + email, e);
        }
        return null;
    }

    public List<User> findAllVoters() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'VOTER' ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAllVoters failed", e);
        }
        return users;
    }

    // ── Email uniqueness check ────────────────────────────

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "emailExists failed", e);
        }
        return false;
    }

    // ── Row mapper ────────────────────────────────────────

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }

    private boolean passwordMatches(String plainPassword, String storedPassword) {
        if (storedPassword == null) return false;
        try {
            if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {
                return BCrypt.checkpw(plainPassword, storedPassword);
            }
            // Backward compatibility for legacy plain-text rows.
            return plainPassword.equals(storedPassword);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Invalid password hash format for a user row.", ex);
            return false;
        }
    }
}
