package com.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.web.daoimpl.CandidateDaoImpl;
import com.web.daoimpl.UserDaoImpl;
import com.web.daoimpl.VotingDaoImpl;
import com.web.pojo.candidate;

@WebServlet("/VotingServlet")
public class VotingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Debug logs
        System.out.println("VotingServlet POST called");
        System.out.println("Session: " + session);
        if (session != null) {
            System.out.println("Username in session: " + session.getAttribute("username"));
        }

        // Check if session or username is invalid
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");

        // Get parameters from form
        String voterId = request.getParameter("voter_id");
        if (voterId != null) voterId = voterId.trim();
        String candidateName = request.getParameter("selected_candidate");

        // DAO instances
        VotingDaoImpl votingDao = new VotingDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        CandidateDaoImpl candidateDao = new CandidateDaoImpl();

        try {
            // Check if voterId belongs to the logged-in user
            if (!userDao.isOwnVoterId(username, voterId)) {
                request.setAttribute("msg", "Security violation: You can only vote with your own registered Voter ID");
                forwardToVotingPage(request, response);
                return;
            }

            // Check if voter ID is valid
            if (!userDao.isValidVoter(voterId)) {
                request.setAttribute("msg", "Invalid Voter ID");
                forwardToVotingPage(request, response);
                return;
            }

            // Get the active election type
            String activeElection = candidateDao.getActiveElectionType();
            if (activeElection == null || activeElection.trim().isEmpty()) {
                request.setAttribute("msg", "No active election available for voting.");
                forwardToVotingPage(request, response);
                return;
            }

            // Store active election in session
            session.setAttribute("activeElectionType", activeElection);

            // Check if user has already voted
            if (votingDao.hasVotedInElection(voterId, activeElection)) {
                request.setAttribute("msg", "You have already voted!");
                forwardToVotingPage(request, response);
                return;
            }

            // Get candidates and validate selected one
            List<candidate> candidates = candidateDao.getCandidatesByElectionType(activeElection);
            boolean validCandidate = candidates.stream()
                    .anyMatch(c -> c.getFullname().equalsIgnoreCase(candidateName));

            if (!validCandidate) {
                request.setAttribute("msg", "Invalid candidate for current election: " + activeElection);
                forwardToVotingPage(request, response);
                return;
            }

            // Record the vote
            if (votingDao.recordVote(voterId, candidateName, activeElection)) {
                userDao.markAsVoted(voterId);
                request.setAttribute("msg", "Thank you for voting for " + candidateName);
            } else {
                request.setAttribute("msg", "Voting failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "System error during voting.");
        }

        // Forward back to the voting page
        forwardToVotingPage(request, response);
    }

    private void forwardToVotingPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/voting.jsp");
        rd.forward(request, response);
    }
}
