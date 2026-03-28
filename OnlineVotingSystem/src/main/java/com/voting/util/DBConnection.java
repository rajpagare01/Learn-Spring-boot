package com.voting.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HikariCP-backed JDBC pool. Credentials from /db.properties.
 * Call {@link #initPool()} from {@link AppStartupListener} before any DAO use.
 */
public final class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String PROPS_FILE = "/db.properties";

    private static volatile HikariDataSource dataSource;

    private DBConnection() {}

    public static synchronized void initPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }
        Properties props = new Properties();
        try (InputStream is = DBConnection.class.getResourceAsStream(PROPS_FILE)) {
            if (is == null) {
                throw new IllegalStateException("Cannot find " + PROPS_FILE + " on classpath.");
            }
            props.load(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.username");
        String pass = props.getProperty("db.password", "");
        String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setDriverClassName(driver);
        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setPoolName("VoteSecurePool");
        cfg.setConnectionTestQuery("SELECT 1");

        dataSource = new HikariDataSource(cfg);
        LOGGER.info("HikariCP pool started.");
    }

    public static synchronized void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("HikariCP pool closed.");
        }
        dataSource = null;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initPool();
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get connection from pool", e);
            throw e;
        }
    }
}
