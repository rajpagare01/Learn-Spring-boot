package com.voting.model;

import java.sql.Timestamp;

/**
 * Maps to the `votes` table.
 */
public class Vote {

    private int       id;
    private int       userId;
    private int       candidateId;
    private int       electionId;
    private Timestamp votedAt;

    public Vote() {}

    public Vote(int id, int userId, int candidateId,
                int electionId, Timestamp votedAt) {
        this.id          = id;
        this.userId      = userId;
        this.candidateId = candidateId;
        this.electionId  = electionId;
        this.votedAt     = votedAt;
    }

    // ── Getters ───────────────────────────────────────────

    public int       getId()          { return id; }
    public int       getUserId()      { return userId; }
    public int       getCandidateId() { return candidateId; }
    public int       getElectionId()  { return electionId; }
    public Timestamp getVotedAt()     { return votedAt; }

    // ── Setters ───────────────────────────────────────────

    public void setId(int id)                  { this.id          = id; }
    public void setUserId(int userId)          { this.userId      = userId; }
    public void setCandidateId(int cid)        { this.candidateId = cid; }
    public void setElectionId(int eid)         { this.electionId  = eid; }
    public void setVotedAt(Timestamp votedAt)  { this.votedAt     = votedAt; }

    @Override
    public String toString() {
        return "Vote{id=" + id + ", userId=" + userId
               + ", candidateId=" + candidateId
               + ", electionId=" + electionId + "}";
    }
}
