package com.voting.util;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private EmailService() {}

    public static boolean isConfigured() {
        return AppConfig.smtpConfigured();
    }

    public static void sendHtml(String to, String subject, String htmlBody) {
        if (!isConfigured()) {
            LOGGER.warning("SMTP not configured; skipping email to " + to);
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", AppConfig.smtpHost());
        props.put("mail.smtp.port", String.valueOf(AppConfig.smtpPort()));
        props.put("mail.smtp.auth", String.valueOf(AppConfig.smtpAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(AppConfig.smtpStartTls()));

        Session session = Session.getInstance(props, AppConfig.smtpAuth()
            ? new jakarta.mail.Authenticator() {
                @Override
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new jakarta.mail.PasswordAuthentication(
                        AppConfig.smtpUser(), AppConfig.smtpPassword());
                }
            }
            : null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(
                AppConfig.mailFromAddress(), AppConfig.mailFromName()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(htmlBody, "text/html; charset=UTF-8");
            try (Transport transport = session.getTransport("smtp")) {
                if (AppConfig.smtpAuth()) {
                    transport.connect(
                        AppConfig.smtpHost(),
                        AppConfig.smtpPort(),
                        AppConfig.smtpUser(),
                        AppConfig.smtpPassword());
                } else {
                    transport.connect();
                }
                transport.sendMessage(msg, msg.getAllRecipients());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "sendHtml failed: " + to, e);
        }
    }
}
