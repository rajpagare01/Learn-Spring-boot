package com.voting.filter;

import com.voting.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminFilter — runs AFTER AuthFilter (ordering via web.xml).
 * Ensures only ADMIN-role users can access /admin/* paths.
 * Voters who try to access admin URLs are redirected to their dashboard.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null && user.isAdmin()) {
            chain.doFilter(request, response);
        } else {
            // Voter tried to access admin area — deny silently
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
        }
    }
}
