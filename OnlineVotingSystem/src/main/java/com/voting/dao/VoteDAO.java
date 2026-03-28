package com.voting.dao;

import com.voting.util.DBConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VoteDAO — handles casting votes and checking vote status.
 *
 * The primary business rule (one vote per user per election) is enforced
 * BOTH at the DB level (UNIQUE constraint) AND here via hasVoted().
 */
public class VoteDAO {

    private static final Logger LOGGER = Logger.getLogger(VoteDAO.class.getName());

    // ── Cast vote ─────────────────────────────────────────

    /**
     * Records a vote. Returns false if the user already voted
     * (caught as a duplicate key violation) or any other error occurs.
     *
     * @return true on success, false on failure
     */
    public boolean castVote(int userId, int candidateId, int electionId) {

        // Application-level guard (faster feedback than waiting for DB error)
        if (hasVoted(userId, electionId)) {
            LOGGER.warning("Duplicate vote attempt: userId=" + userId
                           + " electionId=" + electionId);
            return false;
        }

        String sql = "INSERT INTO votes (user_id, candidate_id, election_id) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, candidateId);
            ps.setInt(3, electionId);

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            // DB-level UNIQUE constraint caught as a safety net
            LOGGER.warning("DB-level duplicate vote blocked: userId=" + userId
                           + " electionId=" + electionId);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "castVote failed", e);
            return false;
        }
    }

    // ── Has voted? ────────────────────────────────────────

    /**
     * Returns true if the user has already cast a vote in this election.
     */
    public boolean hasVoted(int userId, int electionId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE user_id = ? AND election_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, electionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "hasVoted failed", e);
        }
        return false;
    }

    // ── Tallies ───────────────────────────────────────────

    /**
     * Returns the total number of votes cast in a given election.
     */
    public int totalVotesForElection(int electionId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE election_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "totalVotesForElection failed", e);
        }
        return 0;
    }

    /**
     * Returns the vote count for one specific candidate.
     */
    public int votesForCandidate(int candidateId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE candidate_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, candidateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "votesForCandidate failed", e);
        }
        return 0;
    }

    /**
     * Returns the total number of votes cast across all elections (admin stat).
     */
    public int totalVotesAllTime() {
        String sql = "SELECT COUNT(*) FROM votes";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "totalVotesAllTime failed", e);
        }
        return 0;
    }
}
