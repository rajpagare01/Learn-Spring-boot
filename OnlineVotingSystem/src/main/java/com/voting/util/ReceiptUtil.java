package com.voting.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Produces a voter-visible receipt token. Verification checks DB for this hash
 * without exposing the chosen candidate on the verify page.
 */
public final class ReceiptUtil {

    private static final SecureRandom RND = new SecureRandom();

    private ReceiptUtil() {}

    public static String generateReceiptToken(int userId, int electionId, int candidateId) {
        try {
            byte[] salt = new byte[16];
            RND.nextBytes(salt);
            String saltHex = toHex(salt);
            String payload = userId + "|" + electionId + "|" + candidateId + "|" + saltHex;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                AppConfig.receiptSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return toHex(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Could not generate receipt", e);
        }
    }

    public static String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return toHex(md.digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
