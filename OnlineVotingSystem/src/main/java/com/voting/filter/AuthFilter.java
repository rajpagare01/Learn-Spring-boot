package com.voting.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthFilter — ensures every request to protected paths
 * carries a valid session with a logged-in user.
 *
 * Protected paths: /voter/*, /admin/*, /vote, /results
 * Public paths:    /login, /register, /index.jsp, /css/*, /js/*
 */
@WebFilter(urlPatterns = {"/voter/*", "/admin/*", "/vote", "/results"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null
                            && session.getAttribute("user") != null);

        if (loggedIn) {
            // Prevent browser caching of protected pages
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
