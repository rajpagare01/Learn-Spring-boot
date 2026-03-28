package com.voting.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection — thread-safe JDBC connection factory.
 * Reads credentials from /src/main/resources/db.properties.
 * Usage:  try (Connection con = DBConnection.getConnection()) { ... }
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String PROPS_FILE = "/db.properties";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream is = DBConnection.class.getResourceAsStream(PROPS_FILE)) {
            if (is == null) {
                throw new ExceptionInInitializerError(
                    "Cannot find " + PROPS_FILE + " on the classpath.");
            }
            props.load(is);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        URL      = props.getProperty("db.url");
        USER     = props.getProperty("db.username");
        PASSWORD = props.getProperty("db.password");

        try {
            Class.forName(props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() {}

    /**
     * Returns a new JDBC Connection. Callers MUST close it (try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.fine("DB connection opened.");
            return con;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to open DB connection", e);
            throw e;
        }
    }
}
