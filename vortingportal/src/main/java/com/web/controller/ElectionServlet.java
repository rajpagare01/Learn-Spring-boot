package com.web.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.web.daoimpl.ElectionDaoImpl;

@WebServlet("/ElectionServlet")
public class ElectionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ElectionDaoImpl daoimpl = new ElectionDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String electionType = request.getParameter("electionType");
        String target;

        if (electionType == null || electionType.trim().isEmpty()) {
            request.setAttribute("msg", "Please select a valid election type.");
            target = "/electionselect.jsp";
        } else {
            try {
                boolean updated = daoimpl.setActiveElection(electionType);

                if (updated) {
                    HttpSession session = request.getSession();
                    session.setAttribute("activeElectionType", electionType);
                    request.setAttribute("msg", electionType + " election set successfully!");
                    target = "/candidatemanagement.jsp";
                } else {
                    request.setAttribute("msg", "Failed to set active election.");
                    target = "/electionselect.jsp";
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "System error: " + e.getMessage());
                target = "/electionselect.jsp";
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher(target);
        rd.forward(request, response);
    }
}
