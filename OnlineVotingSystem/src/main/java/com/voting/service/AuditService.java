package com.voting.service;

import com.voting.dao.AuditLogDAO;
import jakarta.servlet.http.HttpServletRequest;

public class AuditService {

    private static final AuditLogDAO DAO = new AuditLogDAO();

    private AuditService() {}

    public static void log(String eventType, Integer userId, String detail,
                           HttpServletRequest req) {
        String ip = req != null ? com.voting.util.RequestUtil.clientIp(req) : null;
        DAO.insert(eventType, userId, detail, ip);
    }
}
