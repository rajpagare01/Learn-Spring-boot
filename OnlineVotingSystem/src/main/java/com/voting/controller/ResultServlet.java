package com.voting.controller;

import com.voting.dao.CandidateDAO;
import com.voting.dao.ElectionDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * ResultServlet — displays election results.
 *
 * Results are visible to VOTERS only after an election is CLOSED.
 * Admins can view results at any time.
 *
 * GET /results?electionId=X
 */
@WebServlet("/results")
public class ResultServlet extends HttpServlet {

    private final ElectionDAO  electionDAO  = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO      voteDAO      = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        String idParam = req.getParameter("electionId");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath()
                + (user != null && user.isAdmin()
                   ? "/admin/dashboard" : "/voter/dashboard"));
            return;
        }

        int electionId;
        try {
            electionId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
            return;
        }

        Election election = electionDAO.findById(electionId);
        if (election == null) {
            req.setAttribute("error", "Election not found.");
            req.getRequestDispatcher("/WEB-INF/views/common/error.jsp")
               .forward(req, resp);
            return;
        }

        // Voters may only see results of CLOSED elections
        boolean isAdmin = (user != null && user.isAdmin());
        if (!isAdmin && !election.isClosed()) {
            req.setAttribute("error",
                "Results will be available after the election closes.");
            req.getRequestDispatcher("/WEB-INF/views/common/error.jsp")
               .forward(req, resp);
            return;
        }

        List<Candidate> candidates =
            candidateDAO.findByElectionWithVotes(electionId);
        int totalVotes = voteDAO.totalVotesForElection(electionId);

        boolean hasVoted = (user != null)
                           && voteDAO.hasVoted(user.getId(), electionId);

        req.setAttribute("election",   election);
        req.setAttribute("candidates", candidates);
        req.setAttribute("totalVotes", totalVotes);
        req.setAttribute("hasVoted",   hasVoted);

        String view = isAdmin
            ? "/WEB-INF/views/admin/results.jsp"
            : "/WEB-INF/views/voter/results.jsp";

        req.getRequestDispatcher(view).forward(req, resp);
    }
}
