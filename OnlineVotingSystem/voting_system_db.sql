-- ============================================================
--   ONLINE VOTING SYSTEM — MySQL Schema
--   Compatible with MySQL 8.0+
-- ============================================================

-- 1. Create & select the database
CREATE DATABASE IF NOT EXISTS voting_system
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE voting_system;

-- ============================================================
-- TABLE: users
-- Stores both ADMIN and VOTER accounts
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          INT           NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL,
    email       VARCHAR(150)  NOT NULL,
    password    VARCHAR(255)  NOT NULL,          -- BCrypt hash (60 chars)
    role        ENUM('ADMIN','VOTER') NOT NULL DEFAULT 'VOTER',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users   PRIMARY KEY (id),
    CONSTRAINT uq_email   UNIQUE      (email)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: elections
-- An election has a defined window and lifecycle status
-- ============================================================
CREATE TABLE IF NOT EXISTS elections (
    id          INT           NOT NULL AUTO_INCREMENT,
    title       VARCHAR(200)  NOT NULL,
    description TEXT,
    start_date  DATETIME      NOT NULL,
    end_date    DATETIME      NOT NULL,
    status      ENUM('UPCOMING','ACTIVE','CLOSED') NOT NULL DEFAULT 'UPCOMING',
    created_by  INT           NOT NULL,           -- FK → users(id)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_elections      PRIMARY KEY (id),
    CONSTRAINT uq_election_unique UNIQUE (title, start_date, end_date, created_by),
    CONSTRAINT fk_election_admin FOREIGN KEY (created_by)
        REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_dates CHECK (end_date > start_date)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: candidates
-- Each candidate belongs to exactly one election
-- ============================================================
CREATE TABLE IF NOT EXISTS candidates (
    id          INT           NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL,
    party       VARCHAR(100)  NOT NULL DEFAULT 'Independent',
    bio         TEXT,
    election_id INT           NOT NULL,           -- FK → elections(id)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_candidates        PRIMARY KEY (id),
    CONSTRAINT uq_candidate_unique  UNIQUE (name, party, election_id),
    CONSTRAINT fk_candidate_election FOREIGN KEY (election_id)
        REFERENCES elections(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: votes
-- Core constraint: ONE vote per user per election
-- Enforced by the UNIQUE key (user_id, election_id)
-- ============================================================
CREATE TABLE IF NOT EXISTS votes (
    id           INT       NOT NULL AUTO_INCREMENT,
    user_id      INT       NOT NULL,              -- FK → users(id)
    candidate_id INT       NOT NULL,              -- FK → candidates(id)
    election_id  INT       NOT NULL,              -- FK → elections(id)
    voted_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_votes            PRIMARY KEY (id),
    CONSTRAINT uq_one_vote_per_election
                                   UNIQUE (user_id, election_id),   -- ← THE key constraint
    CONSTRAINT fk_vote_user        FOREIGN KEY (user_id)
        REFERENCES users(id)      ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_vote_candidate   FOREIGN KEY (candidate_id)
        REFERENCES candidates(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_vote_election    FOREIGN KEY (election_id)
        REFERENCES elections(id)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- INDEXES for query performance
-- ============================================================
CREATE INDEX idx_elections_status   ON elections  (status);
CREATE INDEX idx_votes_election     ON votes      (election_id);
CREATE INDEX idx_votes_candidate    ON votes      (candidate_id);
CREATE INDEX idx_candidates_election ON candidates (election_id);

-- ============================================================
-- SAMPLE DATA
-- Passwords are BCrypt hashes of the plaintext shown in comments
-- ============================================================

-- Admin account  (plain: Admin@123)
-- Voter accounts (plain: Voter@123)
INSERT INTO users (name, email, password, role) VALUES
('Super Admin',   'admin@vote.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHy', 'ADMIN'),
('Alice Johnson', 'alice@vote.com',   '$2a$10$EblZqNptyYvcLm/VwDptluAkTK7bGtFYu4r.E5h7h0rUfbKAq7Uuy', 'VOTER'),
('Bob Smith',     'bob@vote.com',     '$2a$10$EblZqNptyYvcLm/VwDptluAkTK7bGtFYu4r.E5h7h0rUfbKAq7Uuy', 'VOTER'),
('Carol White',   'carol@vote.com',   '$2a$10$EblZqNptyYvcLm/VwDptluAkTK7bGtFYu4r.E5h7h0rUfbKAq7Uuy', 'VOTER'),
('David Brown',   'david@vote.com',   '$2a$10$EblZqNptyYvcLm/VwDptluAkTK7bGtFYu4r.E5h7h0rUfbKAq7Uuy', 'VOTER');

-- Elections (created_by = 1 → admin)
INSERT IGNORE INTO elections (title, description, start_date, end_date, status, created_by) VALUES
('Student Council President 2025',
 'Vote for your Student Council President for the academic year 2025-26.',
 '2025-01-01 08:00:00', '2025-12-31 20:00:00', 'ACTIVE', 1),

('Best Sports Captain',
 'Choose the Best Sports Captain of the year.',
 '2025-06-01 08:00:00', '2025-06-30 20:00:00', 'CLOSED', 1),

('Tech Club Lead Election',
 'Elect the new lead of the Technology Club.',
 '2026-04-01 08:00:00', '2026-04-30 20:00:00', 'UPCOMING', 1);

-- Candidates for Election 1
INSERT IGNORE INTO candidates (name, party, bio, election_id) VALUES
('Emma Davis',    'Progressive Party',  'Focused on academic excellence and student welfare.', 1),
('Liam Wilson',   'Unity Alliance',     'Promoting inclusivity and campus development.',       1),
('Sophia Martinez','Green Future',      'Committed to sustainability and campus greening.',     1);

-- Candidates for Election 2
INSERT IGNORE INTO candidates (name, party, bio, election_id) VALUES
('Raj Patel',     'Athletics First',  'Three-time gold medalist in track events.',            2),
('Priya Nair',    'Team Spirit',      'Basketball captain with 5 years experience.',          2);

-- Candidates for Election 3
INSERT IGNORE INTO candidates (name, party, bio, election_id) VALUES
('Chen Wei',      'Innovation Hub',   'AI/ML researcher and hackathon champion.',             3),
('Ananya Singh',  'Open Source Club', 'Open-source contributor and coding workshop leader.', 3);

-- Sample votes (only for ACTIVE / CLOSED elections)
-- Alice votes for Emma in election 1
-- Bob   votes for Liam  in election 1
-- Carol votes for Raj   in election 2
-- David votes for Priya in election 2
INSERT INTO votes (user_id, candidate_id, election_id) VALUES
(2, 1, 1),   -- Alice  → Emma  (Election 1)
(3, 2, 1),   -- Bob    → Liam  (Election 1)
(4, 4, 2),   -- Carol  → Raj   (Election 2)
(5, 5, 2);   -- David  → Priya (Election 2)

-- ============================================================
-- HELPER VIEW: live vote tally per candidate
-- ============================================================
CREATE OR REPLACE VIEW v_vote_tally AS
SELECT
    e.id           AS election_id,
    e.title        AS election_title,
    e.status       AS election_status,
    c.id           AS candidate_id,
    c.name         AS candidate_name,
    c.party        AS candidate_party,
    COUNT(v.id)    AS total_votes
FROM elections  e
JOIN candidates c ON c.election_id = e.id
LEFT JOIN votes v ON v.candidate_id = c.id AND v.election_id = e.id
GROUP BY e.id, e.title, e.status, c.id, c.name, c.party
ORDER BY e.id, total_votes DESC;

-- ============================================================
-- VERIFY
-- ============================================================
SELECT 'users'      AS tbl, COUNT(*) AS rows FROM users
UNION ALL
SELECT 'elections',          COUNT(*) FROM elections
UNION ALL
SELECT 'candidates',         COUNT(*) FROM candidates
UNION ALL
SELECT 'votes',              COUNT(*) FROM votes;

SELECT * FROM v_vote_tally;
