package com.voting.model;

import java.sql.Timestamp;

/**
 * Maps to the `candidates` table.
 */
public class Candidate {

    private int       id;
    private String    name;
    private String    party;
    private String    bio;
    private int       electionId;  // FK → elections.id
    private Timestamp createdAt;

    // Transient field: populated by VoteDAO for results pages
    private int voteCount;

    public Candidate() {}

    public Candidate(int id, String name, String party,
                     String bio, int electionId, Timestamp createdAt) {
        this.id         = id;
        this.name       = name;
        this.party      = party;
        this.bio        = bio;
        this.electionId = electionId;
        this.createdAt  = createdAt;
    }

    // ── Getters ───────────────────────────────────────────

    public int       getId()         { return id; }
    public String    getName()       { return name; }
    public String    getParty()      { return party; }
    public String    getBio()        { return bio; }
    public int       getElectionId() { return electionId; }
    public Timestamp getCreatedAt()  { return createdAt; }
    public int       getVoteCount()  { return voteCount; }

    // ── Setters ───────────────────────────────────────────

    public void setId(int id)                { this.id         = id; }
    public void setName(String name)         { this.name       = name; }
    public void setParty(String party)       { this.party      = party; }
    public void setBio(String bio)           { this.bio        = bio; }
    public void setElectionId(int eid)       { this.electionId = eid; }
    public void setCreatedAt(Timestamp ts)   { this.createdAt  = ts; }
    public void setVoteCount(int voteCount)  { this.voteCount  = voteCount; }

    @Override
    public String toString() {
        return "Candidate{id=" + id + ", name='" + name
               + "', party='" + party + "', electionId=" + electionId + "}";
    }
}
