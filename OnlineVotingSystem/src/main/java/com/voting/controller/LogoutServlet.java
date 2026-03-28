package com.voting.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LogoutServlet — invalidates the session and redirects to login.
 * GET /logout → invalidate session → redirect to /login
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear any remember-me cookies if added in future
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("JSESSIONID".equals(c.getName())) {
                    c.setMaxAge(0);
                    c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                    resp.addCookie(c);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/login?logout=true");
    }
}
