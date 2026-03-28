package com.web.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.daoimpl.ModifyVoterDaoImpl;
import com.web.pojo.ModifyVoter;


@WebServlet("/UpgardeServlet")
public class UpgardeServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String username = request.getParameter("username");
		    String password = request.getParameter("password");
		    String fullName = request.getParameter("full_name");
		    int age = Integer.parseInt(request.getParameter("age"));
		    String mobile = request.getParameter("mobile");
		    int voterId = Integer.parseInt(request.getParameter("voter_id"));  // make sure this field is included in form!

		    ModifyVoter v = new ModifyVoter();
		    v.setUsername(username);
		    v.setPassword(password);
		    v.setFullname(fullName);
		    v.setAge(age);
		    v.setMobile(mobile);
		    v.setVoter_id(voterId);

		    ModifyVoterDaoImpl dao = new ModifyVoterDaoImpl();
		    boolean success = dao.updateVoter(v);

		    if(success) {
		        request.setAttribute("msg", "Voter updated successfully!");
		    } else {
		        request.setAttribute("msg", "Failed to update voter.");
		    }
		    request.getRequestDispatcher("upgradeVoter.jsp?voter_id=" + voterId).forward(request, response);
	}

}
