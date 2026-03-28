package com.voting.controller;

import com.voting.dao.UserDAO;
import com.voting.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet — authenticates a user and creates a session.
 * GET  /login → show login.jsp
 * POST /login → validate credentials, set session, redirect by role
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Already logged in?
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            redirectByRole((User) session.getAttribute("user"), req, resp);
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email    = trim(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");

        if (email.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.loginUser(email, password);

        if (user == null) {
            req.setAttribute("error", "Invalid email or password.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // ── Create session (invalidate old one first to prevent fixation) ─
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) oldSession.invalidate();

        HttpSession session = req.getSession(true);
        session.setAttribute("user",   user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role",   user.getRole());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        redirectByRole(user, req, resp);
    }

    private void redirectByRole(User user, HttpServletRequest req,
                                HttpServletResponse resp) throws IOException {
        String ctx = req.getContextPath();
        if (user.isAdmin()) {
            resp.sendRedirect(ctx + "/admin/dashboard");
        } else {
            resp.sendRedirect(ctx + "/voter/dashboard");
        }
    }

    private String trim(String s) { return (s == null) ? "" : s.trim(); }
}
