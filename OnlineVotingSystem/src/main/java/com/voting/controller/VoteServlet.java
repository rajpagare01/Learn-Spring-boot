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
 * VoteServlet — displays the ballot for an active election and records the vote.
 *
 * GET  /vote?electionId=X → show ballot (vote.jsp)
 * POST /vote              → record vote, redirect to results or dashboard
 */
@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    private final ElectionDAO  electionDAO  = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO      voteDAO      = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = getSessionUser(req);
        String idParam = req.getParameter("electionId");

        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
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

        // Guard: election must exist and be ACTIVE
        if (election == null || !election.isActive()) {
            req.setAttribute("error", "This election is not currently active.");
            req.getRequestDispatcher("/WEB-INF/views/common/error.jsp")
               .forward(req, resp);
            return;
        }

        // Guard: user must not have already voted
        if (voteDAO.hasVoted(user.getId(), electionId)) {
            req.setAttribute("info", "You have already voted in this election.");
            req.setAttribute("electionId", electionId);
            resp.sendRedirect(req.getContextPath()
                              + "/results?electionId=" + electionId);
            return;
        }

        List<Candidate> candidates = candidateDAO.findByElection(electionId);

        req.setAttribute("election",   election);
        req.setAttribute("candidates", candidates);
        req.getRequestDispatcher("/WEB-INF/views/voter/vote.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        User user = getSessionUser(req);

        String electionIdParam  = req.getParameter("electionId");
        String candidateIdParam = req.getParameter("candidateId");

        if (electionIdParam == null || candidateIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
            return;
        }

        int electionId, candidateId;
        try {
            electionId  = Integer.parseInt(electionIdParam);
            candidateId = Integer.parseInt(candidateIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/voter/dashboard");
            return;
        }

        Election election = electionDAO.findById(electionId);

        // Guard: election must still be ACTIVE at time of POST
        if (election == null || !election.isActive()) {
            req.setAttribute("error", "This election has closed.");
            req.getRequestDispatcher("/WEB-INF/views/common/error.jsp")
               .forward(req, resp);
            return;
        }

        // Guard: candidate must belong to this election
        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null || candidate.getElectionId() != electionId) {
            req.setAttribute("error", "Invalid candidate selection.");
            req.getRequestDispatcher("/WEB-INF/views/common/error.jsp")
               .forward(req, resp);
            return;
        }

        boolean voted = voteDAO.castVote(user.getId(), candidateId, electionId);

        if (voted) {
            resp.sendRedirect(req.getContextPath()
                              + "/voter/dashboard?voted=true");
        } else {
            req.setAttribute("error",
                "Your vote could not be recorded. You may have already voted.");
            List<Candidate> candidates = candidateDAO.findByElection(electionId);
            req.setAttribute("election",   election);
            req.setAttribute("candidates", candidates);
            req.getRequestDispatcher("/WEB-INF/views/voter/vote.jsp")
               .forward(req, resp);
        }
    }

    private User getSessionUser(HttpServletRequest req) {
        return (User) req.getSession(false).getAttribute("user");
    }
}
