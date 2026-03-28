package com.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.daoimpl.ModifyVoterDaoImpl;
import com.web.pojo.ModifyVoter;

@SuppressWarnings("serial")
@WebServlet("/GetAllVoterServlet")
public class GetAllVoterServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ModifyVoterDaoImpl daoimpl = new ModifyVoterDaoImpl();
		List<ModifyVoter>lst=daoimpl.getModifyVoters();
		request.setAttribute("allvoter", lst);
		
		RequestDispatcher rd=request.getRequestDispatcher("/voterlist.jsp");
		rd.forward(request, response);
	}

}
