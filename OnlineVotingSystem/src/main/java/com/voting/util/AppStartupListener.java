package com.voting.util;

import com.voting.dao.ElectionDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

/**
 * AppStartupListener — runs once when the web application starts.
 *
 * Responsibilities:
 *  - Sync all election statuses based on current datetime
 *  - Verify DB connectivity on startup (fail-fast)
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

    private static final Logger LOGGER =
        Logger.getLogger(AppStartupListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("=== VoteSecure starting up ===");

        // Verify DB is reachable
        try {
            DBConnection.getConnection().close();
            LOGGER.info("Database connection: OK");
        } catch (Exception e) {
            LOGGER.severe("Database connection FAILED on startup: " + e.getMessage());
        }

        // Sync election statuses
        try {
            new ElectionDAO().syncElectionStatuses();
            LOGGER.info("Election statuses synced.");
        } catch (Exception e) {
            LOGGER.warning("Could not sync election statuses: " + e.getMessage());
        }

        LOGGER.info("=== VoteSecure ready ===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("=== VoteSecure shutting down ===");
    }
}
