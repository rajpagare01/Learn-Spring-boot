package com.voting.dao;

import com.voting.model.Candidate;
import com.voting.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CandidateDAO — CRUD operations for the candidates table.
 */
public class CandidateDAO {

    private static final Logger LOGGER = Logger.getLogger(CandidateDAO.class.getName());

    // ── Create ────────────────────────────────────────────

    public boolean addCandidate(Candidate c) {
        String sql = "INSERT INTO candidates (name, party, bio, election_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getParty());
            ps.setString(3, c.getBio());
            ps.setInt(4, c.getElectionId());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) c.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "addCandidate failed", e);
        }
        return false;
    }

    // ── Read ──────────────────────────────────────────────

    public Candidate findById(int id) {
        String sql = "SELECT * FROM candidates WHERE id = ?";
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

    public List<Candidate> findByElection(int electionId) {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM candidates WHERE election_id = ? ORDER BY name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByElection failed: " + electionId, e);
        }
        return list;
    }

    /**
     * Returns candidates with vote counts for a given election.
     * Results are sorted descending by votes (for results page).
     */
    public List<Candidate> findByElectionWithVotes(int electionId) {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT c.*, COUNT(v.id) AS vote_count "
                   + "FROM candidates c "
                   + "LEFT JOIN votes v ON v.candidate_id = c.id AND v.election_id = c.election_id "
                   + "WHERE c.election_id = ? "
                   + "GROUP BY c.id "
                   + "ORDER BY vote_count DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Candidate c = mapRow(rs);
                    c.setVoteCount(rs.getInt("vote_count"));
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByElectionWithVotes failed: " + electionId, e);
        }
        return list;
    }

    // ── Update ────────────────────────────────────────────

    public boolean updateCandidate(Candidate c) {
        String sql = "UPDATE candidates SET name=?, party=?, bio=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getParty());
            ps.setString(3, c.getBio());
            ps.setInt(4, c.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "updateCandidate failed: " + c.getId(), e);
        }
        return false;
    }

    // ── Delete ────────────────────────────────────────────

    public boolean deleteCandidate(int id) {
        String sql = "DELETE FROM candidates WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteCandidate failed: " + id, e);
        }
        return false;
    }

    // ── Row mapper ────────────────────────────────────────

    private Candidate mapRow(ResultSet rs) throws SQLException {
        Candidate c = new Candidate();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setParty(rs.getString("party"));
        c.setBio(rs.getString("bio"));
        c.setElectionId(rs.getInt("election_id"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
