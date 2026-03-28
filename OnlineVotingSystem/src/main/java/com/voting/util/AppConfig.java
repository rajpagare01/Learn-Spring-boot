package com.voting.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads /app.properties from the classpath (SMTP, URLs, feature flags).
 */
public final class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = AppConfig.class.getResourceAsStream("/app.properties")) {
            if (is != null) {
                PROPS.load(is);
            }
        } catch (IOException ignored) {
            // defaults used
        }
    }

    private AppConfig() {}

    public static String get(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

    public static String baseUrl() {
        return get("app.base.url", "http://localhost:8080/OnlineVotingSystem").replaceAll("/$", "");
    }

    public static String receiptSecret() {
        return get("app.receipt.secret", "change-this-receipt-secret-in-production");
    }

    public static boolean loginOtpEnabled() {
        return Boolean.parseBoolean(get("app.login.otp.enabled", "true"));
    }

    public static boolean emailVerificationRequired() {
        return Boolean.parseBoolean(get("app.email.verification.required", "false"));
    }

    public static String uploadDir() {
        return get("app.upload.dir", "").trim();
    }

    public static String smtpHost() {
        return get("mail.smtp.host", "").trim();
    }

    public static int smtpPort() {
        try {
            return Integer.parseInt(get("mail.smtp.port", "587"));
        } catch (NumberFormatException e) {
            return 587;
        }
    }

    public static String smtpUser() {
        return get("mail.smtp.user", "").trim();
    }

    public static String smtpPassword() {
        return get("mail.smtp.password", "").trim();
    }

    public static boolean smtpAuth() {
        return Boolean.parseBoolean(get("mail.smtp.auth", "true"));
    }

    public static boolean smtpStartTls() {
        return Boolean.parseBoolean(get("mail.smtp.starttls", "true"));
    }

    public static String mailFromAddress() {
        return get("mail.from.address", "noreply@votesecure.local");
    }

    public static String mailFromName() {
        return get("mail.from.name", "VoteSecure");
    }

    public static boolean smtpConfigured() {
        return !smtpHost().isEmpty();
    }
}
