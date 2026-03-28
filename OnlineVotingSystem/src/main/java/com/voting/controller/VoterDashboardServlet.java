package com.voting.controller;

import com.voting.dao.ElectionDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Election;
import com.voting.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VoterDashboardServlet
 *
 * GET /voter/dashboard:
 *  1. Syncs election statuses from DB dates
 *  2. Loads active, closed, upcoming elections
 *  3. Builds votedMap (electionId -> boolean) for current user
 *  4. Forwards to voter/dashboard.jsp
 */
@WebServlet("/voter/dashboard")
public class VoterDashboardServlet extends HttpServlet {

    private final ElectionDAO electionDAO = new ElectionDAO();
    private final VoteDAO     voteDAO     = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        electionDAO.syncElectionStatuses();

        User user = (User) req.getSession(false).getAttribute("user");

        List<Election> activeElections   = electionDAO.findActiveElections();
        List<Election> closedElections   = electionDAO.findClosedElections();
        List<Election> upcomingElections = electionDAO.findUpcomingElections();

        Map<Integer, Boolean> votedMap = new HashMap<>();
        for (Election e : activeElections) {
            votedMap.put(e.getId(), voteDAO.hasVoted(user.getId(), e.getId()));
        }
        for (Election e : closedElections) {
            votedMap.put(e.getId(), voteDAO.hasVoted(user.getId(), e.getId()));
        }

        req.setAttribute("activeElections",   activeElections);
        req.setAttribute("closedElections",   closedElections);
        req.setAttribute("upcomingElections", upcomingElections);
        req.setAttribute("votedMap",          votedMap);

        req.getRequestDispatcher("/WEB-INF/views/voter/dashboard.jsp")
           .forward(req, resp);
    }
}
