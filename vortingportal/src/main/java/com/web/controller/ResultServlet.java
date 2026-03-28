package com.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.web.daoimpl.CandidateDaoImpl;
import com.web.daoimpl.VotingDaoImpl;
import com.web.pojo.candidate;

import java.util.List;

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String electionType = request.getParameter("electionType");
        if (electionType == null || electionType.trim().isEmpty()) {
            electionType = "Default"; // fallback or set an error
        }

        VotingDaoImpl votingDao = new VotingDaoImpl();
        CandidateDaoImpl candidateDao = new CandidateDaoImpl();

        try {
            List<candidate> candidates = candidateDao.getCandidatesByElectionType(electionType);
            int totalVotes = votingDao.getTotalVotesByElectionType(electionType);

            request.setAttribute("candidates", candidates);
            request.setAttribute("totalVotes", totalVotes);
            request.setAttribute("electionType", electionType);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading results: " + e.getMessage());
        }

        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }
}
