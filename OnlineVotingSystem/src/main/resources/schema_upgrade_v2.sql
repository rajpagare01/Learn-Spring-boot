-- ============================================================
-- VoteSecure v2 schema upgrade — run ONCE on existing DB
--   mysql -u root -p voting_system < schema_upgrade_v2.sql
-- ============================================================
USE voting_system;

ALTER TABLE users
    ADD COLUMN email_verified TINYINT(1) NOT NULL DEFAULT 1,
    ADD COLUMN active TINYINT(1) NOT NULL DEFAULT 1,
    ADD COLUMN profile_photo VARCHAR(500) NULL;

ALTER TABLE elections
    ADD COLUMN round_num INT NOT NULL DEFAULT 1,
    ADD COLUMN parent_election_id INT NULL,
    ADD COLUMN closing_reminder_sent TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN opening_notice_sent TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN results_email_sent TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE elections
    ADD CONSTRAINT fk_election_parent
        FOREIGN KEY (parent_election_id) REFERENCES elections(id)
        ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE candidates
    ADD COLUMN photo_path VARCHAR(500) NULL;

ALTER TABLE votes
    ADD COLUMN receipt_hash VARCHAR(128) NULL;

CREATE UNIQUE INDEX uq_votes_receipt_hash ON votes (receipt_hash);

CREATE TABLE IF NOT EXISTS otp_tokens (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT          NOT NULL,
    code_hash  VARCHAR(128) NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_otp_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_otp_user_expires (user_id, expires_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT          NOT NULL,
    token      VARCHAR(64)  NOT NULL,
    expires_at DATETIME     NOT NULL,
    used       TINYINT(1)   NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_pwd_reset_token UNIQUE (token),
    CONSTRAINT fk_pwd_reset_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS email_verify_tokens (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT          NOT NULL,
    token      VARCHAR(64)  NOT NULL,
    expires_at DATETIME     NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_email_verify_token UNIQUE (token),
    CONSTRAINT fk_email_verify_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_log (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type  VARCHAR(64)  NOT NULL,
    user_id     INT          NULL,
    detail      TEXT         NULL,
    ip_address  VARCHAR(45)  NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_created (created_at),
    INDEX idx_audit_user (user_id),
    INDEX idx_audit_type (event_type)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS election_categories (
    election_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (election_id, category_id),
    CONSTRAINT fk_ec_election FOREIGN KEY (election_id)
        REFERENCES elections(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ec_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS election_eligible_voters (
    election_id INT NOT NULL,
    user_id     INT NOT NULL,
    PRIMARY KEY (election_id, user_id),
    CONSTRAINT fk_eev_election FOREIGN KEY (election_id)
        REFERENCES elections(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_eev_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

SELECT 'schema_upgrade_v2 applied' AS status;
