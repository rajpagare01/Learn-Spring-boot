package com.voting.model;

import java.sql.Timestamp;

/**
 * Maps to the `users` table.
 */
public class User {

    private int       id;
    private String    name;
    private String    email;
    private String    password;   // BCrypt hash — never plain text
    private String    role;       // "ADMIN" | "VOTER"
    private Timestamp createdAt;

    public User() {}

    public User(int id, String name, String email, String password,
                String role, Timestamp createdAt) {
        this.id        = id;
        this.name      = name;
        this.email     = email;
        this.password  = password;
        this.role      = role;
        this.createdAt = createdAt;
    }

    // ── Getters ───────────────────────────────────────────

    public int       getId()        { return id; }
    public String    getName()      { return name; }
    public String    getEmail()     { return email; }
    public String    getPassword()  { return password; }
    public String    getRole()      { return role; }
    public Timestamp getCreatedAt() { return createdAt; }

    // ── Setters ───────────────────────────────────────────

    public void setId(int id)               { this.id        = id; }
    public void setName(String name)        { this.name      = name; }
    public void setEmail(String email)      { this.email     = email; }
    public void setPassword(String pw)      { this.password  = pw; }
    public void setRole(String role)        { this.role      = role; }
    public void setCreatedAt(Timestamp ts)  { this.createdAt = ts; }

    // ── Helpers ───────────────────────────────────────────

    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', role='" + role + "'}";
    }
}
