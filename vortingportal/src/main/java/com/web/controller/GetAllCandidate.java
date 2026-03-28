package com.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.daoimpl.CandidateDaoImpl;
import com.web.pojo.candidate;

@SuppressWarnings("serial")
@WebServlet("/GetAllCandidate")
public class GetAllCandidate extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CandidateDaoImpl daoimpl = new CandidateDaoImpl();
		List<candidate>candidate=daoimpl.getCandidates();
		request.setAttribute("allcandidate", candidate);
		RequestDispatcher rd= request.getRequestDispatcher("/candidatelist.jsp");
		rd.forward(request, response);
	}

}
