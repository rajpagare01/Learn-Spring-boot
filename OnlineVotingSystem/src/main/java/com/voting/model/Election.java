package com.voting.model;

import java.sql.Timestamp;

/**
 * Maps to the `elections` table.
 */
public class Election {

    private int       id;
    private String    title;
    private String    description;
    private Timestamp startDate;
    private Timestamp endDate;
    private String    status;      // "UPCOMING" | "ACTIVE" | "CLOSED"
    private int       createdBy;   // FK → users.id
    private Timestamp createdAt;

    public Election() {}

    public Election(int id, String title, String description,
                    Timestamp startDate, Timestamp endDate,
                    String status, int createdBy, Timestamp createdAt) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.status      = status;
        this.createdBy   = createdBy;
        this.createdAt   = createdAt;
    }

    // ── Getters ───────────────────────────────────────────

    public int       getId()          { return id; }
    public String    getTitle()       { return title; }
    public String    getDescription() { return description; }
    public Timestamp getStartDate()   { return startDate; }
    public Timestamp getEndDate()     { return endDate; }
    public String    getStatus()      { return status; }
    public int       getCreatedBy()   { return createdBy; }
    public Timestamp getCreatedAt()   { return createdAt; }

    // ── Setters ───────────────────────────────────────────

    public void setId(int id)                    { this.id          = id; }
    public void setTitle(String title)           { this.title       = title; }
    public void setDescription(String desc)      { this.description = desc; }
    public void setStartDate(Timestamp startDate){ this.startDate   = startDate; }
    public void setEndDate(Timestamp endDate)    { this.endDate     = endDate; }
    public void setStatus(String status)         { this.status      = status; }
    public void setCreatedBy(int createdBy)      { this.createdBy   = createdBy; }
    public void setCreatedAt(Timestamp ts)       { this.createdAt   = ts; }

    // ── Helpers ───────────────────────────────────────────

    public boolean isActive()   { return "ACTIVE".equalsIgnoreCase(status); }
    public boolean isClosed()   { return "CLOSED".equalsIgnoreCase(status); }
    public boolean isUpcoming() { return "UPCOMING".equalsIgnoreCase(status); }

    @Override
    public String toString() {
        return "Election{id=" + id + ", title='" + title + "', status='" + status + "'}";
    }
}
