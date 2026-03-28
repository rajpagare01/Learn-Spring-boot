package com.voting.controller;

import com.voting.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * RegisterServlet — handles new voter registration.
 * GET  /register → show register.jsp
 * POST /register → validate, create user, redirect to login
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in, redirect to appropriate dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
            return;
        }
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String name     = trim(req.getParameter("name"));
        String email    = trim(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirmPassword");

        // ── Server-side validation ────────────────────────
        String error = validate(name, email, password, confirm);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // ── Duplicate email check ─────────────────────────
        if (userDAO.emailExists(email)) {
            req.setAttribute("error", "An account with this email already exists.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // ── Persist ───────────────────────────────────────
        boolean success = userDAO.registerUser(name, email, password);
        if (success) {
            resp.sendRedirect(req.getContextPath()
                              + "/login?registered=true");
        } else {
            req.setAttribute("error", "Registration failed. Please try again.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    // ── Helpers ───────────────────────────────────────────

    private String validate(String name, String email,
                            String password, String confirm) {
        if (name == null || name.isEmpty())
            return "Full name is required.";
        if (name.length() < 2 || name.length() > 100)
            return "Name must be between 2 and 100 characters.";
        if (email == null || !email.matches("^[\\w.+\\-]+@[a-zA-Z\\d\\-]+\\.[a-zA-Z]{2,}$"))
            return "Please enter a valid email address.";
        if (password == null || password.length() < 8)
            return "Password must be at least 8 characters.";
        if (!password.equals(confirm))
            return "Passwords do not match.";
        return null;
    }

    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }
}
