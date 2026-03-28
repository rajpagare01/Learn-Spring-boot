package com.web.controller;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.daoimpl.UserDaoImpl;
import com.web.daoimpl.CandidateDaoImpl;
import com.web.pojo.User;

@SuppressWarnings("serial")
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServerException, IOException, ServletException {
        
        User u = new User();
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password"));

        UserDaoImpl daoimpl = new UserDaoImpl();
        boolean isValid = daoimpl.checkUserCredential(u);

        String target = "";

        if (isValid) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            session = request.getSession(true);
            session.setAttribute("username", u.getUsername());

            CandidateDaoImpl candidateDao = new CandidateDaoImpl();
            String activeElectionType = candidateDao.getActiveElectionType();
            if (activeElectionType != null && !activeElectionType.isEmpty()) {
                session.setAttribute("activeElectionType", activeElectionType);
            }

            target = "/voter.jsp";
        } else {
            request.setAttribute("msg", "<font size=2 color=red>invalid username/password</font>");
            target = "/login.jsp";
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(target);
        rd.forward(request, response);
    }
}
